package com.mxraven.mail;

import com.mxraven.mail.model.BodyType;
import com.mxraven.mail.model.Content;
import com.mxraven.mail.model.DSNRecipientParams;
import com.mxraven.mail.model.DeliveryBy;
import com.mxraven.mail.model.DeliveryByMode;
import com.mxraven.mail.model.Envelope;
import com.mxraven.mail.model.Mail;
import com.mxraven.mail.model.Path;
import com.mxraven.mail.model.Recipient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A synchronous SMTP submission client.
 *
 * <p>Connect with {@link #connect(SmtpConfig)} and send a built
 * {@link Mail} with {@link #send(Mail)} or a prebuilt RFC 5322 message with
 * {@link #sendRaw(Envelope, byte[])}. A client holds an open socket, so close
 * it when finished, for example in a try-with-resources block.
 *
 * <p>A client is <strong>not</strong> safe for concurrent use. To reuse
 * connections across threads, use {@link SmtpPool}.
 *
 * <p>A blocked exchange can be aborted from another thread with {@link #cancel()},
 * which closes the socket and maps the resulting failure to an
 * {@link SmtpException}. A blocked caller can also be interrupted.
 */
public final class SmtpClient implements AutoCloseable {
    private static final ScheduledThreadPoolExecutor WRITE_WATCHDOG = createWriteWatchdog();

    private static ScheduledThreadPoolExecutor createWriteWatchdog() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, runnable -> {
            Thread thread = new Thread(runnable, "mxraven-smtp-write-timeout");
            thread.setDaemon(true);
            return thread;
        });
        // Completed writes cancel their watchdog; drop cancelled tasks immediately
        // instead of retaining them until the (possibly long) write timeout.
        executor.setRemoveOnCancelPolicy(true);
        return executor;
    }

    private final SmtpConfig config;
    private Socket socket;
    private BufferedReader in;
    private OutputStream out;
    private String greeting;
    private boolean esmtp;
    private boolean tls;
    private boolean authenticated;
    private boolean closed;
    private volatile boolean cancelled;
    private Map<String, String> extensions = Map.of();
    private SmtpResponse lastResponse;

    private SmtpClient(SmtpConfig config) {
        this.config = config;
    }

    /**
     * Opens a connection to the configured server, reads the greeting, performs
     * the ESMTP handshake, upgrades to TLS when required, and authenticates
     * when credentials are configured.
     *
     * @param config the connection configuration
     * @return a connected client
     * @throws IOException when the connection or SMTP exchange fails
     * @throws SmtpException when the server rejects a required capability
     */
    public static SmtpClient connect(SmtpConfig config) throws IOException {
        SmtpClient client = new SmtpClient(config);
        client.openSocket();
        client.greeting = client.readResponse().message();
        client.ehlo();
        if (config.security() == SecurityMode.STARTTLS) {
            if (!client.hasExtension("STARTTLS")) {
                throw new SmtpException("server does not advertise STARTTLS");
            }
            client.startTls();
            client.ehlo();
        }
        if (config.username() != null && !config.username().isBlank()) {
            client.auth();
        }
        return client;
    }

    /**
     * Returns the server greeting read when the connection opened.
     *
     * @return the greeting text
     */
    public String greeting() {
        return greeting;
    }

    /**
     * Reports whether the session is protected by TLS.
     *
     * @return {@code true} when TLS is active
     */
    public boolean isTls() {
        return tls;
    }

    /**
     * Reports whether the server accepted the EHLO command.
     *
     * @return {@code true} when ESMTP extensions are available
     */
    public boolean isEsmtp() {
        return esmtp;
    }

    /**
     * Reports whether the client authenticated successfully.
     *
     * @return {@code true} when authentication succeeded
     */
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * Reports whether the server advertised an ESMTP extension.
     *
     * @param name the extension name, matched case-insensitively
     * @return {@code true} when the extension is advertised
     */
    public boolean hasExtension(String name) {
        return extensions.containsKey(name.toUpperCase(Locale.ROOT));
    }

    /**
     * Returns the parameter line the server advertised for an ESMTP extension.
     *
     * @param name the extension name, matched case-insensitively
     * @return the extension parameter, or {@code null} when not advertised
     */
    public String extensionParam(String name) {
        return extensions.get(name.toUpperCase(Locale.ROOT));
    }

    /**
     * Returns the advertised ESMTP extensions.
     *
     * @return an immutable map of extension name to parameter line
     */
    public Map<String, String> extensions() {
        return Map.copyOf(extensions);
    }

    /**
     * Returns the most recent server reply.
     *
     * @return the last response, or {@code null} before any reply was read
     */
    public SmtpResponse lastResponse() {
        return lastResponse;
    }

    /**
     * Sends a built message using its envelope and serialized content.
     *
     * @param mail the message to send
     * @return the send result
     * @throws IOException when the SMTP exchange fails
     */
    public SendResult send(Mail mail) throws IOException {
        ensureActive();
        return deliver(mail.envelope(), serialize(mail.content()));
    }

    /**
     * Sends a prebuilt RFC 5322 message with the given SMTP envelope.
     *
     * @param envelope the SMTP envelope for the message
     * @param rawMessage the raw RFC 5322 message bytes
     * @return the send result
     * @throws IOException when the SMTP exchange fails
     */
    public SendResult sendRaw(Envelope envelope, byte[] rawMessage) throws IOException {
        ensureActive();
        return deliver(envelope, rawMessage);
    }

    private SendResult deliver(Envelope envelope, byte[] message) throws IOException {
        ensureActive();
        String mailCommand = mailFromCommand(envelope);
        List<Recipient> recipients = envelope.to();
        List<RecipientResult> results = new ArrayList<>();
        List<SmtpResponse> rcptResponses = new ArrayList<>(recipients.size());

        boolean pipelining = hasExtension("PIPELINING") && recipients.size() > 1;
        SmtpResponse fromResponse;
        if (pipelining) {
            // Batch MAIL FROM and every RCPT TO, then drain the replies. This is
            // the higher-latency benefit RFC 2920 provides.
            writeCommand(mailCommand);
            for (Recipient recipient : recipients) {
                writeCommand(rcptToCommand(recipient));
            }
            fromResponse = readResponse();
            for (int i = 0; i < recipients.size(); i++) {
                rcptResponses.add(readResponse());
            }
        } else {
            fromResponse = cmd(mailCommand);
            for (Recipient recipient : recipients) {
                rcptResponses.add(rcptTo(recipient));
            }
        }

        boolean anyAccepted = false;
        for (int i = 0; i < recipients.size(); i++) {
            SmtpResponse response = rcptResponses.get(i);
            boolean accepted = response.isSuccess();
            anyAccepted |= accepted;
            results.add(new RecipientResult(recipients.get(i), accepted, response.message()));
        }

        if (!fromResponse.isSuccess()) {
            safeRset();
            return new SendResult(false, results, fromResponse.message());
        }
        if (!anyAccepted) {
            safeRset();
            return new SendResult(false, results, "all recipients were rejected");
        }

        SmtpResponse transactionResponse;
        if (hasExtension("CHUNKING")) {
            transactionResponse = bdat(message, true);
        } else {
            SmtpResponse dataResponse = cmd("DATA");
            if (dataResponse.code() != 354) {
                safeRset();
                return new SendResult(false, results, dataResponse.message());
            }
            writeMessageBody(message);
            transactionResponse = readResponse();
        }

        boolean success = transactionResponse.isSuccess();
        if (!success) {
            safeRset();
        }
        return new SendResult(success, results, transactionResponse.message());
    }

    /**
     * Issues a {@code MAIL FROM} command with the given reverse path.
     *
     * @param address the sender address, without angle brackets
     * @return the server response
     * @throws IOException when the SMTP exchange fails
     */
    public SmtpResponse mail(String address) throws IOException {
        rejectCommandInjection(address, "address");
        return cmd("MAIL FROM:<" + address + ">");
    }

    /**
     * Issues a {@code RCPT TO} command with the given forward path.
     *
     * @param address the recipient address, without angle brackets
     * @return the server response
     * @throws IOException when the SMTP exchange fails
     */
    public SmtpResponse rcpt(String address) throws IOException {
        rejectCommandInjection(address, "address");
        return cmd("RCPT TO:<" + address + ">");
    }

    /**
     * Issues a {@code DATA} command and writes the message content, stuffing
     * leading dots and terminating the payload with a CRLF-dot-CRLF sequence.
     * When the server does not reply with code {@code 354}, the content is not
     * transmitted.
     *
     * @param content the raw message content
     * @return the response to the data command
     * @throws IOException when the SMTP exchange fails
     */
    public SmtpResponse data(byte[] content) throws IOException {
        ensureActive();
        SmtpResponse response = cmd("DATA");
        if (response.code() != 354) {
            return response;
        }
        writeMessageBody(content);
        return readResponse();
    }

    /**
     * Transfers message content with the RFC 3030 {@code BDAT} command, used
     * instead of {@code DATA} when the server advertises {@code CHUNKING}. The
     * payload is written verbatim with no dot-stuffing.
     *
     * @param chunk the content chunk
     * @param last  whether this is the final chunk of the message
     * @return the server response
     * @throws IOException when the SMTP exchange fails
     */
    public SmtpResponse bdat(byte[] chunk, boolean last) throws IOException {
        ensureActive();
        byte[] payload = chunk == null ? new byte[0] : chunk;
        writeCommand("BDAT " + payload.length + (last ? " LAST" : ""));
        writeBytes(payload);
        return readResponse();
    }

    /**
     * Issues a {@code NOOP} command.
     *
     * @return the server response
     * @throws IOException when the SMTP exchange fails
     */
    public SmtpResponse noop() throws IOException {
        return cmd("NOOP");
    }

    /**
     * Issues a {@code RSET} command to reset the current transaction.
     *
     * @return the server response
     * @throws IOException when the SMTP exchange fails
     */
    public SmtpResponse reset() throws IOException {
        return cmd("RSET");
    }

    /**
     * Issues a {@code QUIT} command and closes the connection.
     *
     * <p>When the client is already closed, a synthetic code {@code 221}
     * response is returned without contacting the server.
     *
     * @return the server's farewell response
     * @throws IOException when the SMTP exchange fails
     */
    public SmtpResponse quit() throws IOException {
        if (closed) {
            return new SmtpResponse(221, "Bye");
        }
        SmtpResponse response = cmd("QUIT");
        close();
        return response;
    }

    /**
     * Aborts the current or next exchange. The connection is closed and any
     * blocked operation fails with an {@link SmtpException}. This is a best-effort
     * cancellation intended to be called from another thread. The client is not
     * reusable afterwards.
     */
    public void cancel() {
        cancelled = true;
        Socket current = socket;
        if (current != null) {
            try {
                current.close();
            } catch (IOException ignored) {
                // The socket is being cancelled; closing is best effort.
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (!closed) {
            closed = true;
            socket.close();
        }
    }

    private void ensureOpen() throws IOException {
        if (closed) {
            throw new SmtpException("client is closed");
        }
    }

    private void ensureActive() throws IOException {
        ensureOpen();
        if (cancelled) {
            throw new SmtpException("operation cancelled");
        }
        if (Thread.currentThread().isInterrupted()) {
            cancelled = true;
            throw new SmtpException("operation interrupted");
        }
    }

    private void openSocket() throws IOException {
        Socket s = new Socket();
        try {
            s.connect(new InetSocketAddress(config.host(), config.port()),
                    (int) config.connectTimeout().toMillis());
            s.setSoTimeout((int) config.readTimeout().toMillis());
        } catch (IOException e) {
            s.close();
            throw e;
        }

        if (config.security() == SecurityMode.IMPLICIT_TLS) {
            s = sslContext().getSocketFactory().createSocket(s, config.host(), config.port(), true);
            tls = true;
        }
        this.socket = s;
        this.in = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
        this.out = s.getOutputStream();
    }

    private void rebind(Socket s) throws IOException {
        this.socket = s;
        this.in = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
        this.out = s.getOutputStream();
    }

    private void startTls() throws IOException {
        SmtpResponse response = cmd("STARTTLS");
        if (response.code() != 220) {
            throw new SmtpException("STARTTLS failed: " + response.message());
        }
        SSLSocket ssl = (SSLSocket) sslContext().getSocketFactory()
                .createSocket(socket, config.host(), config.port(), true);
        ssl.setUseClientMode(true);
        ssl.startHandshake();
        rebind(ssl);
        tls = true;
        esmtp = false;
        authenticated = false;
        extensions = Map.of();
    }

    private SSLContext sslContext() {
        if (config.sslContext() != null) {
            return config.sslContext();
        }
        try {
            return SSLContext.getDefault();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new SmtpException("TLS is not available", e);
        }
    }

    private void ehlo() throws IOException {
        SmtpResponse response = cmd("EHLO " + config.localName());
        if (response.isSuccess()) {
            esmtp = true;
            extensions = parseExtensions(response.message());
        } else {
            response = cmd("HELO " + config.localName());
            if (response.isSuccess()) {
                esmtp = false;
                extensions = Map.of();
            } else {
                throw new SmtpException("EHLO/HELO rejected: " + response.message());
            }
        }
    }

    private void auth() throws IOException {
        String advertised = extensionParam("AUTH");
        if (advertised == null || advertised.isBlank()) {
            throw new SmtpException("server does not advertise AUTH");
        }
        Set<String> mechanisms = new HashSet<>(Arrays.asList(advertised.toUpperCase(Locale.ROOT).split(" ")));
        if (mechanisms.contains("PLAIN")) {
            authPlain();
        } else if (mechanisms.contains("LOGIN")) {
            authLogin();
        } else {
            throw new SmtpException("server does not advertise a supported AUTH mechanism: " + advertised);
        }
    }

    private void authPlain() throws IOException {
        String token = "\u0000" + config.username() + "\u0000" + config.password();
        String encoded = Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
        SmtpResponse response = cmd("AUTH PLAIN " + encoded);
        if (response.code() != 235) {
            throw new SmtpException("AUTH failed: " + response.message());
        }
        authenticated = true;
    }

    private void authLogin() throws IOException {
        SmtpResponse response = cmd("AUTH LOGIN");
        if (response.code() != 334) {
            throw new SmtpException("AUTH failed: " + response.message());
        }
        response = cmd(Base64.getEncoder().encodeToString(config.username().getBytes(StandardCharsets.UTF_8)));
        if (response.code() != 334) {
            throw new SmtpException("AUTH failed: " + response.message());
        }
        response = cmd(Base64.getEncoder().encodeToString(config.password().getBytes(StandardCharsets.UTF_8)));
        if (response.code() != 235) {
            throw new SmtpException("AUTH failed: " + response.message());
        }
        authenticated = true;
    }

    private String mailFromCommand(Envelope envelope) {
        Path from = envelope.from() == null ? Path.nullPath() : envelope.from();
        List<String> params = new ArrayList<>();

        if (envelope.size() > 0 && hasExtension("SIZE")) {
            params.add("SIZE=" + envelope.size());
        }
        if (envelope.bodyType() == BodyType.EIGHT_BIT_MIME && hasExtension("8BITMIME")) {
            params.add("BODY=8BITMIME");
        } else if (envelope.bodyType() == BodyType.BINARY_MIME && hasExtension("BINARYMIME")) {
            params.add("BODY=BINARYMIME");
        }
        if (envelope.smtpUtf8() && hasExtension("SMTPUTF8")) {
            params.add("SMTPUTF8");
        }
        if (envelope.requireTls()) {
            if (!tls) {
                throw new SmtpException("REQUIRETLS requires an active TLS session");
            }
            if (!hasExtension("REQUIRETLS")) {
                throw new SmtpException("server does not support REQUIRETLS");
            }
            params.add("REQUIRETLS");
        }
        if (envelope.deliveryBy() != null) {
            if (!hasExtension("DELIVERBY")) {
                throw new SmtpException("server does not support DELIVERBY");
            }
            params.add("BY=" + formatDeliveryBy(envelope.deliveryBy()));
        }
        if (envelope.auth() != null && !envelope.auth().isBlank()) {
            rejectCommandInjection(envelope.auth(), "AUTH identity");
            params.add("AUTH=<" + envelope.auth() + ">");
        }
        if (envelope.dsnParams() != null
                && envelope.dsnParams().ret() != null
                && !envelope.dsnParams().ret().isBlank()
                && hasExtension("DSN")) {
            params.add("RET=" + normalizeRet(envelope.dsnParams().ret()));
        }
        if (envelope.envId() != null && !envelope.envId().isBlank() && hasExtension("DSN")) {
            String envId = DsnXText.encode(envelope.envId());
            if (envId.length() > 100) {
                throw new SmtpException("DSN ENVID must not exceed 100 characters when encoded");
            }
            params.add("ENVID=" + envId);
        }
        for (Map.Entry<String, String> entry : envelope.extensionParams().entrySet()) {
            if (entry.getKey().equalsIgnoreCase("BY") && envelope.deliveryBy() != null) {
                continue;
            }
            rejectCommandInjection(entry.getKey(), "extension parameter name");
            rejectCommandInjection(entry.getValue(), "extension parameter value");
            params.add(entry.getValue() == null || entry.getValue().isEmpty()
                    ? entry.getKey()
                    : entry.getKey() + "=" + entry.getValue());
        }

        String command = "MAIL FROM:" + from;
        if (!params.isEmpty()) {
            command += " " + String.join(" ", params);
        }
        return command;
    }

    private SmtpResponse rcptTo(Recipient recipient) throws IOException {
        return cmd(rcptToCommand(recipient));
    }

    private String rcptToCommand(Recipient recipient) {
        List<String> params = new ArrayList<>();
        DSNRecipientParams dsn = recipient.dsnParams();
        if (dsn != null && hasExtension("DSN")) {
            List<String> notify = normalizeNotify(dsn.notifyFlags());
            if (!notify.isEmpty()) {
                params.add("NOTIFY=" + String.join(",", notify));
            }
            if (dsn.orcpt() != null && !dsn.orcpt().isBlank()) {
                params.add("ORCPT=" + formatOrcpt(dsn.orcpt()));
            }
        }

        String command = "RCPT TO:" + recipient.address();
        if (!params.isEmpty()) {
            command += " " + String.join(" ", params);
        }
        return command;
    }

    private static String formatDeliveryBy(DeliveryBy deliveryBy) {
        DeliveryByMode mode = deliveryBy.mode() == null ? DeliveryByMode.NOTIFY : deliveryBy.mode();
        String value = deliveryBy.seconds() + ";" + mode.wire();
        return deliveryBy.trace() ? value + "T" : value;
    }

    private static String normalizeRet(String ret) {
        String token = ret.trim().toUpperCase(Locale.ROOT);
        if (!token.equals("FULL") && !token.equals("HDRS")) {
            throw new SmtpException("invalid DSN RET: " + ret);
        }
        return token;
    }

    private static List<String> normalizeNotify(List<String> flags) {
        List<String> out = new ArrayList<>();
        if (flags == null) {
            return out;
        }
        for (String flag : flags) {
            if (flag == null || flag.isBlank()) {
                continue;
            }
            String token = flag.trim().toUpperCase(Locale.ROOT);
            boolean valid = token.equals("NEVER") || token.equals("SUCCESS")
                    || token.equals("FAILURE") || token.equals("DELAY");
            if (!valid) {
                throw new SmtpException("invalid DSN NOTIFY flag: " + flag);
            }
            if (!out.contains(token)) {
                out.add(token);
            }
        }
        if (out.contains("NEVER") && out.size() > 1) {
            throw new SmtpException("DSN NOTIFY=NEVER must not be combined with other flags");
        }
        return out;
    }

    /**
     * Formats an ORCPT value as {@code addr-type;xtext} (RFC 3461). When the input
     * does not carry an address type, {@code rfc822} is assumed.
     */
    private static String formatOrcpt(String orcpt) {
        int semicolon = orcpt.indexOf(';');
        if (semicolon <= 0) {
            return "rfc822;" + DsnXText.encode(orcpt);
        }
        String addressType = orcpt.substring(0, semicolon);
        if (!isAddressType(addressType)) {
            throw new SmtpException("invalid ORCPT address type: " + addressType);
        }
        return addressType + ";" + DsnXText.encode(orcpt.substring(semicolon + 1));
    }

    private static boolean isAddressType(String value) {
        if (value.isEmpty()) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            boolean valid = (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')
                    || (c >= '0' && c <= '9') || c == '-';
            if (!valid) {
                return false;
            }
        }
        return true;
    }

    private SmtpResponse cmd(String command) throws IOException {
        ensureActive();
        writeCommand(command);
        return readResponse();
    }

    private void writeCommand(String command) throws IOException {
        if (command.indexOf('\r') >= 0 || command.indexOf('\n') >= 0) {
            throw new SmtpException("SMTP command must not contain CR or LF");
        }
        writeBytes((command + "\r\n").getBytes(StandardCharsets.UTF_8));
    }

    private static void rejectCommandInjection(String text, String what) {
        if (text == null) {
            return;
        }
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\r' || c == '\n' || c == '\0') {
                throw new SmtpException(what + " must not contain CR, LF, or NUL characters");
            }
        }
    }

    private void writeMessageBody(byte[] content) throws IOException {
        writeBytes(dotStuff(content));
    }

    private static byte[] dotStuff(byte[] content) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(content.length + 16);
        boolean atLineStart = true;
        for (byte b : content) {
            if (atLineStart && b == '.') {
                buffer.write('.');
            }
            buffer.write(b);
            atLineStart = b == '\n';
        }
        if (content.length == 0 || content[content.length - 1] != '\n') {
            buffer.write('\r');
            buffer.write('\n');
        }
        buffer.write('.');
        buffer.write('\r');
        buffer.write('\n');
        return buffer.toByteArray();
    }

    private void writeBytes(byte[] data) throws IOException {
        ensureActive();
        long timeoutMillis = config.writeTimeout() == null ? 0 : config.writeTimeout().toMillis();
        if (timeoutMillis <= 0) {
            out.write(data);
            out.flush();
            return;
        }
        AtomicBoolean finished = new AtomicBoolean(false);
        Socket current = socket;
        ScheduledFuture<?> watchdog = WRITE_WATCHDOG.schedule(() -> {
            if (finished.compareAndSet(false, true) && current != null) {
                try {
                    current.close();
                } catch (IOException ignored) {
                    // Closing the socket is the timeout action; failure is ignored.
                }
            }
        }, timeoutMillis, TimeUnit.MILLISECONDS);
        try {
            out.write(data);
            out.flush();
        } catch (IOException e) {
            if (finished.get() && !closed && !cancelled) {
                throw new SocketTimeoutException("write timed out after " + timeoutMillis + "ms");
            }
            throw e;
        } finally {
            if (!finished.getAndSet(true)) {
                watchdog.cancel(false);
            }
        }
    }

    private String readLine() throws IOException {
        try {
            String line = in.readLine();
            if (line == null && cancelled) {
                throw new SmtpException("operation cancelled");
            }
            return line;
        } catch (IOException e) {
            if (cancelled) {
                throw new SmtpException("operation cancelled");
            }
            throw e;
        }
    }

    private SmtpResponse readResponse() throws IOException {
        String first = readLine();
        if (first == null) {
            throw new IOException("connection closed by server");
        }
        if (first.length() < 3) {
            throw new SmtpException("malformed SMTP response: " + first);
        }
        int code;
        try {
            code = Integer.parseInt(first.substring(0, 3));
        } catch (NumberFormatException e) {
            throw new SmtpException("malformed SMTP response: " + first);
        }

        boolean multiline = first.length() > 3 && first.charAt(3) == '-';
        StringBuilder message = new StringBuilder();
        appendLine(message, first.substring(3));
        if (multiline) {
            String terminator = first.substring(0, 3) + " ";
            boolean terminated = false;
            String line;
            while ((line = readLine()) != null) {
                if (line.startsWith(terminator)) {
                    appendLine(message, line.substring(4));
                    terminated = true;
                    break;
                }
                if (line.length() > 4 && line.startsWith(first.substring(0, 3) + "-")) {
                    appendLine(message, line.substring(4));
                } else {
                    appendLine(message, line);
                }
            }
            if (!terminated) {
                throw new IOException("connection closed before end of SMTP response");
            }
        }
        SmtpResponse response = new SmtpResponse(code, message.toString().trim());
        this.lastResponse = response;
        return response;
    }

    private static void appendLine(StringBuilder message, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        if (!message.isEmpty()) {
            message.append('\n');
        }
        message.append(value.trim());
    }

    private static Map<String, String> parseExtensions(String message) {
        Map<String, String> result = new LinkedHashMap<>();
        String[] lines = message.split("\n");
        for (int i = 0; i < lines.length; i++) {
            // The first line of an EHLO reply is the server greeting, not an
            // extension. readResponse already stripped the reply code.
            if (i == 0) {
                continue;
            }
            String value = lines[i].trim();
            if (value.isBlank()) {
                continue;
            }
            int space = value.indexOf(' ');
            String name = (space < 0 ? value : value.substring(0, space)).toUpperCase(Locale.ROOT);
            String param = space < 0 ? "" : value.substring(space + 1).trim();
            if (!name.isEmpty()) {
                result.put(name, param);
            }
        }
        return result;
    }

    private void safeRset() {
        if (closed || cancelled) {
            return;
        }
        try {
            writeCommand("RSET");
            readResponse();
        } catch (IOException e) {
            cancelled = true;
        }
    }

    private static byte[] serialize(Content content) throws IOException {
        return normalizeCrlf(content.toRaw());
    }

    private static byte[] normalizeCrlf(byte[] data) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(data.length + 16);
        for (int i = 0; i < data.length; i++) {
            byte b = data[i];
            if (b == '\r') {
                if (i + 1 < data.length && data[i + 1] == '\n') {
                    buffer.write('\r');
                    buffer.write('\n');
                    i++;
                } else {
                    buffer.write('\r');
                    buffer.write('\n');
                }
            } else if (b == '\n') {
                buffer.write('\r');
                buffer.write('\n');
            } else {
                buffer.write(b);
            }
        }
        return buffer.toByteArray();
    }
}
