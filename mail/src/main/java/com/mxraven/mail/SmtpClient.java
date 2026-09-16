package com.mxraven.mail;

import com.mxraven.mail.internal.Java8;

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

/**
 * A synchronous SMTP submission client.
 *
 * <p>Connect with {@link #connect(SmtpConfig)} and send a built
 * {@link Mail} with {@link #send(Mail)} or a prebuilt RFC 5322 message with
 * {@link #sendRaw(Envelope, byte[])}. A client holds an open socket, so close
 * it when finished, for example in a try-with-resources block.
 */
public final class SmtpClient implements AutoCloseable {
    private final SmtpConfig config;
    private Socket socket;
    private BufferedReader in;
    private OutputStream out;
    private String greeting;
    private boolean esmtp;
    private boolean tls;
    private boolean authenticated;
    private boolean closed;
    private Map<String, String> extensions = Java8.map();
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
        if (config.username() != null && !Java8.isBlank(config.username())) {
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
        return Java8.copyMap(extensions);
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
        ensureOpen();
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
        ensureOpen();
        return deliver(envelope, rawMessage);
    }

    private SendResult deliver(Envelope envelope, byte[] message) throws IOException {
        SmtpResponse fromResponse = mailFrom(envelope);
        List<RecipientResult> results = new ArrayList<>();
        for (Recipient recipient : envelope.to()) {
            SmtpResponse rcptResponse = rcptTo(recipient);
            results.add(new RecipientResult(recipient, rcptResponse.isSuccess(), rcptResponse.message()));
        }

        SmtpResponse dataResponse = data(message);
        boolean success = fromResponse.isSuccess() && dataResponse.isSuccess();
        return new SendResult(success, results, dataResponse.message());
    }

    /**
     * Issues a {@code MAIL FROM} command with the given reverse path.
     *
     * @param address the sender address, without angle brackets
     * @return the server response
     * @throws IOException when the SMTP exchange fails
     */
    public SmtpResponse mail(String address) throws IOException {
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
        SmtpResponse response = cmd("DATA");
        if (response.code() != 354) {
            return response;
        }
        writeDotStuffed(content);
        if (content.length == 0 || content[content.length - 1] != '\n') {
            out.write("\r\n".getBytes(StandardCharsets.UTF_8));
        }
        out.write(".\r\n".getBytes(StandardCharsets.UTF_8));
        out.flush();
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
        extensions = Java8.map();
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
                extensions = Java8.map();
            } else {
                throw new SmtpException("EHLO/HELO rejected: " + response.message());
            }
        }
    }

    private void auth() throws IOException {
        String advertised = extensionParam("AUTH");
        if (advertised == null || Java8.isBlank(advertised)) {
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

    private SmtpResponse mailFrom(Envelope envelope) throws IOException {
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
        if (envelope.auth() != null && !Java8.isBlank(envelope.auth())) {
            params.add("AUTH=<" + envelope.auth() + ">");
        }
        if (envelope.dsnParams() != null
                && envelope.dsnParams().ret() != null
                && !Java8.isBlank(envelope.dsnParams().ret())
                && hasExtension("DSN")) {
            params.add("RET=" + normalizeRet(envelope.dsnParams().ret()));
        }
        if (envelope.envId() != null && !Java8.isBlank(envelope.envId()) && hasExtension("DSN")) {
            params.add("ENVID=" + DsnXText.encode(envelope.envId()));
        }
        for (Map.Entry<String, String> entry : envelope.extensionParams().entrySet()) {
            if (entry.getKey().equalsIgnoreCase("BY") && envelope.deliveryBy() != null) {
                continue;
            }
            params.add(entry.getValue() == null || entry.getValue().isEmpty()
                    ? entry.getKey()
                    : entry.getKey() + "=" + entry.getValue());
        }

        String command = "MAIL FROM:" + from;
        if (!params.isEmpty()) {
            command += " " + String.join(" ", params);
        }
        return cmd(command);
    }

    private SmtpResponse rcptTo(Recipient recipient) throws IOException {
        List<String> params = new ArrayList<>();
        DSNRecipientParams dsn = recipient.dsnParams();
        if (dsn != null && hasExtension("DSN")) {
            List<String> notify = normalizeNotify(dsn.notifyFlags());
            if (!notify.isEmpty()) {
                params.add("NOTIFY=" + String.join(",", notify));
            }
            if (dsn.orcpt() != null && !Java8.isBlank(dsn.orcpt())) {
                params.add("ORCPT=" + formatOrcpt(dsn.orcpt()));
            }
        }

        String command = "RCPT TO:" + recipient.address();
        if (!params.isEmpty()) {
            command += " " + String.join(" ", params);
        }
        return cmd(command);
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
            if (flag == null) {
                continue;
            }
            String token = flag.trim().toUpperCase(Locale.ROOT);
            boolean valid = token.equals("NEVER") || token.equals("SUCCESS")
                    || token.equals("FAILURE") || token.equals("DELAY");
            if (valid && !out.contains(token)) {
                out.add(token);
            }
        }
        return out;
    }

    private static String formatOrcpt(String orcpt) {
        int semicolon = orcpt.indexOf(';');
        if (semicolon < 0) {
            return DsnXText.encode(orcpt);
        }
        return orcpt.substring(0, semicolon) + ";" + DsnXText.encode(orcpt.substring(semicolon + 1));
    }

    private SmtpResponse cmd(String command) throws IOException {
        out.write((command + "\r\n").getBytes(StandardCharsets.UTF_8));
        out.flush();
        return readResponse();
    }

    private SmtpResponse readResponse() throws IOException {
        String first = in.readLine();
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
            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith(terminator)) {
                    appendLine(message, line.substring(4));
                    break;
                }
                if (line.length() > 4 && line.startsWith(first.substring(0, 3) + "-")) {
                    appendLine(message, line.substring(4));
                } else {
                    appendLine(message, line);
                }
            }
        }
        SmtpResponse response = new SmtpResponse(code, message.toString().trim());
        this.lastResponse = response;
        return response;
    }

    private static void appendLine(StringBuilder message, String value) {
        if (value == null || Java8.isBlank(value)) {
            return;
        }
        if (message.length() != 0) {
            message.append('\n');
        }
        message.append(value.trim());
    }

    private static Map<String, String> parseExtensions(String message) {
        Map<String, String> result = new LinkedHashMap<>();
        for (String line : message.split("\n")) {
            String value = line.trim();
            if (Java8.isBlank(value)) {
                continue;
            }
            int space = value.indexOf(' ');
            String name = (space < 0 ? value : value.substring(0, space)).toUpperCase(Locale.ROOT);
            String param = space < 0 ? "" : value.substring(space + 1).trim();
            if (!name.isEmpty() && !name.equals("250")) {
                result.put(name, param);
            }
        }
        return result;
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

    private void writeDotStuffed(byte[] content) throws IOException {
        boolean atLineStart = true;
        for (byte b : content) {
            if (atLineStart && b == '.') {
                out.write('.');
            }
            out.write(b);
            atLineStart = b == '\n';
        }
    }
}
