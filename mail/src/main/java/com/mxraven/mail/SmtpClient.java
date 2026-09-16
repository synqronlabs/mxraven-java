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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
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

/**
 * A synchronous SMTP submission client.
 *
 * <p>Connect with {@link #connect(SmtpConfig)} and send a built
 * {@link Mail} with {@link #send(Mail)} or a prebuilt RFC 5322 message with
 * {@link #sendRaw(Envelope, byte[])} / {@link #sendRaw(Envelope, InputStream)}.
 * A client holds an open socket, so close it when finished, for example in a
 * try-with-resources block.
 *
 * <p>A client is <strong>not</strong> safe for concurrent use. To reuse
 * connections across threads, use {@link SmtpPool}.
 *
 * <p>A blocked exchange can be aborted from another thread with {@link #cancel()},
 * which closes the socket and maps the resulting failure to an
 * {@link SmtpException}. A single send can instead be bounded with
 * {@link SendOptions} (a deadline and/or a {@link Cancellation}).
 */
public final class SmtpClient implements AutoCloseable {
    private static final ScheduledThreadPoolExecutor MONITOR = createMonitor();
    private static final long MONITOR_TICK_NANOS = 250_000_000L;
    private static final byte[] CRLF = {'\r', '\n'};
    private static final byte[] DOT_TERMINATOR = {'.', '\r', '\n'};
    private static final int STREAM_CHUNK = 65536;

    private static ScheduledThreadPoolExecutor createMonitor() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, runnable -> {
            Thread thread = new Thread(runnable, "mxraven-smtp-monitor");
            thread.setDaemon(true);
            return thread;
        });
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

    private volatile boolean operationActive;
    private volatile boolean writing;
    private volatile boolean watchdogClosed;
    private volatile long operationDeadlineNanos = Long.MAX_VALUE;
    private volatile long lastProgressNanos;
    private volatile Cancellation activeCancellation;
    private ScheduledFuture<?> monitorFuture;

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
        return send(mail, SendOptions.defaults());
    }

    /**
     * Sends a built message with per-send options.
     *
     * @param mail    the message to send
     * @param options the deadline and/or cancellation, or {@code null} for none
     * @return the send result
     * @throws IOException when the SMTP exchange fails or the deadline passes
     */
    public SendResult send(Mail mail, SendOptions options) throws IOException {
        ensureOpen();
        startOperation(options);
        try {
            byte[] message = serialize(mail.content());
            return deliver(mail.envelope(), () -> transferBytes(message));
        } finally {
            endOperation();
        }
    }

    /**
     * Sends a prebuilt RFC 5322 message with the given SMTP envelope.
     *
     * <p>The bytes are written as-is; unlike {@link #send(Mail)} they are not
     * CRLF-normalized.
     *
     * @param envelope the SMTP envelope for the message
     * @param rawMessage the raw RFC 5322 message bytes
     * @return the send result
     * @throws IOException when the SMTP exchange fails
     */
    public SendResult sendRaw(Envelope envelope, byte[] rawMessage) throws IOException {
        return sendRaw(envelope, rawMessage, SendOptions.defaults());
    }

    /**
     * Sends a prebuilt message with per-send options.
     *
     * @param envelope   the SMTP envelope
     * @param rawMessage the raw RFC 5322 message bytes
     * @param options    the deadline and/or cancellation, or {@code null} for none
     * @return the send result
     * @throws IOException when the SMTP exchange fails or the deadline passes
     */
    public SendResult sendRaw(Envelope envelope, byte[] rawMessage, SendOptions options) throws IOException {
        ensureOpen();
        startOperation(options);
        try {
            return deliver(envelope, () -> transferBytes(rawMessage));
        } finally {
            endOperation();
        }
    }

    /**
     * Streams a prebuilt RFC 5322 message from {@code rawMessage} with the given
     * SMTP envelope.
     *
     * <p>The stream is consumed as the message is transferred and is
     * <strong>not closed</strong> by this method. The bytes are written as-is;
     * they are not CRLF-normalized. Because the length is unknown up front, no
     * {@code SIZE} parameter is derived; set {@link Envelope#size()} if the
     * server requires one.
     *
     * @param envelope   the SMTP envelope for the message
     * @param rawMessage the raw RFC 5322 message stream
     * @return the send result
     * @throws IOException when the SMTP exchange or the stream read fails
     */
    public SendResult sendRaw(Envelope envelope, InputStream rawMessage) throws IOException {
        return sendRaw(envelope, rawMessage, SendOptions.defaults());
    }

    /**
     * Streams a prebuilt message with per-send options.
     *
     * @param envelope   the SMTP envelope
     * @param rawMessage the raw RFC 5322 message stream, not closed
     * @param options    the deadline and/or cancellation, or {@code null} for none
     * @return the send result
     * @throws IOException when the SMTP exchange or the stream read fails
     */
    public SendResult sendRaw(Envelope envelope, InputStream rawMessage, SendOptions options)
            throws IOException {
        ensureOpen();
        if (rawMessage == null) {
            throw new IllegalArgumentException("rawMessage is required");
        }
        startOperation(options);
        try {
            return deliver(envelope, () -> transferStream(envelope, rawMessage));
        } finally {
            endOperation();
        }
    }

    private SendResult deliver(Envelope envelope, MessageTransfer transfer) throws IOException {
        ensureActive();
        String mailCommand = mailFromCommand(envelope);
        List<Recipient> recipients = envelope.to();
        List<RecipientResult> results = new ArrayList<>();
        List<SmtpResponse> rcptResponses = new ArrayList<>(recipients.size());

        boolean pipelining = hasExtension("PIPELINING") && recipients.size() > 1;
        SmtpResponse fromResponse;
        if (pipelining) {
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
            // Do not issue RCPT TO after a rejected MAIL FROM: the transaction is
            // not open, and the server answers with a bad-sequence error.
            if (fromResponse.isSuccess()) {
                for (Recipient recipient : recipients) {
                    rcptResponses.add(rcptTo(recipient));
                }
            }
        }

        if (!fromResponse.isSuccess()) {
            safeRset();
            return new SendResult(false, List.of(), fromResponse.message());
        }

        boolean anyAccepted = false;
        for (int i = 0; i < recipients.size(); i++) {
            SmtpResponse response = rcptResponses.get(i);
            boolean accepted = response.isSuccess();
            anyAccepted |= accepted;
            results.add(new RecipientResult(recipients.get(i), accepted, response.message()));
        }

        if (!anyAccepted) {
            safeRset();
            return new SendResult(false, results, "all recipients were rejected");
        }

        SmtpResponse transactionResponse = transfer.send();
        boolean success = transactionResponse.isSuccess();
        if (!success) {
            safeRset();
        }
        return new SendResult(success, results, transactionResponse.message());
    }

    private SmtpResponse transferBytes(byte[] message) throws IOException {
        if (hasExtension("CHUNKING")) {
            return bdatChunk(message, true);
        }
        SmtpResponse dataResponse = cmd("DATA");
        if (dataResponse.code() != 354) {
            return dataResponse;
        }
        writeMessageBody(message);
        return readResponse();
    }

    private SmtpResponse transferStream(Envelope envelope, InputStream message) throws IOException {
        long size = envelope.size();
        if (hasExtension("CHUNKING") && size > 0) {
            // A single BDAT ... LAST is the form the buffered path uses and the
            // most widely supported; the body is still read and written in
            // chunks, so nothing is buffered.
            return bdatStreamKnownSize(message, size);
        }
        SmtpResponse dataResponse = cmd("DATA");
        if (dataResponse.code() != 354) {
            return dataResponse;
        }
        writeDotStuffedStream(message);
        return readResponse();
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
        startOperation(null);
        try {
            return cmd("MAIL FROM:<" + address + ">");
        } finally {
            endOperation();
        }
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
        startOperation(null);
        try {
            return cmd("RCPT TO:<" + address + ">");
        } finally {
            endOperation();
        }
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
        startOperation(null);
        try {
            SmtpResponse response = cmd("DATA");
            if (response.code() != 354) {
                return response;
            }
            writeMessageBody(content);
            return readResponse();
        } finally {
            endOperation();
        }
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
        startOperation(null);
        try {
            return bdatChunk(chunk, last);
        } finally {
            endOperation();
        }
    }

    private SmtpResponse bdatChunk(byte[] chunk, boolean last) throws IOException {
        byte[] payload = chunk == null ? new byte[0] : chunk;
        writeCommand("BDAT " + payload.length + (last ? " LAST" : ""));
        writeBytes(payload);
        return readResponse();
    }

    private SmtpResponse bdatStreamKnownSize(InputStream source, long size) throws IOException {
        writeCommand("BDAT " + size + " LAST");
        byte[] buffer = new byte[STREAM_CHUNK];
        long remaining = size;
        while (remaining > 0) {
            int toRead = (int) Math.min(buffer.length, remaining);
            int read = source.read(buffer, 0, toRead);
            if (read == -1) {
                throw new IOException("message stream ended after " + (size - remaining)
                        + " of " + size + " declared bytes");
            }
            if (read == 0) {
                continue;
            }
            writeBytes(buffer, 0, read);
            remaining -= read;
        }
        return readResponse();
    }

    /**
     * Issues a {@code NOOP} command.
     *
     * @return the server response
     * @throws IOException when the SMTP exchange fails
     */
    public SmtpResponse noop() throws IOException {
        startOperation(null);
        try {
            return cmd("NOOP");
        } finally {
            endOperation();
        }
    }

    /**
     * Issues a {@code RSET} command to reset the current transaction.
     *
     * @return the server response
     * @throws IOException when the SMTP exchange fails
     */
    public SmtpResponse reset() throws IOException {
        startOperation(null);
        try {
            return cmd("RSET");
        } finally {
            endOperation();
        }
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
        closeQuietly();
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
        Cancellation cancellation = activeCancellation;
        if (cancellation != null && cancellation.isCancelled()) {
            throw new SmtpException("operation cancelled");
        }
        if (operationDeadlineNanos != Long.MAX_VALUE && System.nanoTime() >= operationDeadlineNanos) {
            throw new SocketTimeoutException("operation timed out");
        }
    }

    private void startOperation(SendOptions options) {
        SendOptions opts = options == null ? SendOptions.defaults() : options;
        this.activeCancellation = opts.cancellation().orElse(null);
        Duration timeout = opts.timeout().orElse(null);
        this.operationDeadlineNanos = timeout == null ? Long.MAX_VALUE
                : System.nanoTime() + timeout.toNanos();
        this.lastProgressNanos = System.nanoTime();
        this.watchdogClosed = false;
        this.operationActive = true;
        armMonitor();
    }

    private void endOperation() {
        this.operationActive = false;
        this.activeCancellation = null;
        this.operationDeadlineNanos = Long.MAX_VALUE;
        ScheduledFuture<?> future = monitorFuture;
        monitorFuture = null;
        if (future != null) {
            future.cancel(false);
        }
    }

    private void armMonitor() {
        if (!monitorNeeded()) {
            return;
        }
        monitorFuture = MONITOR.schedule(this::monitorTick, MONITOR_TICK_NANOS, TimeUnit.NANOSECONDS);
    }

    private boolean monitorNeeded() {
        Duration writeTimeout = config.writeTimeout();
        return activeCancellation != null
                || (writeTimeout != null && !writeTimeout.isZero() && !writeTimeout.isNegative())
                || operationDeadlineNanos != Long.MAX_VALUE;
    }

    private void monitorTick() {
        if (!operationActive) {
            return;
        }
        Cancellation cancellation = activeCancellation;
        if (cancellation != null && cancellation.isCancelled()) {
            cancelled = true;
            closed = true;
            closeQuietly();
            return;
        }
        long now = System.nanoTime();
        boolean timedOut = operationDeadlineNanos != Long.MAX_VALUE && now >= operationDeadlineNanos;
        Duration writeTimeout = config.writeTimeout();
        boolean stalled = writing && writeTimeout != null
                && !writeTimeout.isZero() && !writeTimeout.isNegative()
                && now - lastProgressNanos > writeTimeout.toNanos();
        if (timedOut || stalled) {
            watchdogClosed = true;
            closed = true;
            closeQuietly();
            return;
        }
        if (operationActive && !closed) {
            monitorFuture = MONITOR.schedule(this::monitorTick, MONITOR_TICK_NANOS, TimeUnit.NANOSECONDS);
        }
    }

    private void closeQuietly() {
        Socket current = socket;
        if (current != null) {
            try {
                current.close();
            } catch (IOException ignored) {
                // Closing is best effort.
            }
        }
    }

    /**
     * Checks that an idle pooled connection is still usable by issuing a
     * {@code NOOP} with a short timeout. A failed probe closes the client.
     *
     * @param timeoutMillis the probe timeout in milliseconds
     * @return {@code true} when the server answered with a 2xx reply
     */
    boolean probe(long timeoutMillis) {
        if (closed || cancelled || socket == null || in == null || out == null) {
            return false;
        }
        try {
            int previous = socket.getSoTimeout();
            socket.setSoTimeout((int) Math.max(1L, Math.min(timeoutMillis, Integer.MAX_VALUE)));
            try {
                out.write("NOOP\r\n".getBytes(StandardCharsets.UTF_8));
                out.flush();
                String line = in.readLine();
                if (line == null) {
                    closed = true;
                    closeQuietly();
                    return false;
                }
                return line.length() >= 1 && line.charAt(0) == '2';
            } finally {
                if (!closed) {
                    socket.setSoTimeout(previous);
                }
            }
        } catch (IOException e) {
            closed = true;
            closeQuietly();
            return false;
        }
    }

    private void openSocket() throws IOException {
        Socket s = config.proxy() == null ? new Socket() : new Socket(config.proxy());
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
        if (config.oauthToken() != null) {
            authXoauth2();
            return;
        }
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

    private void authXoauth2() throws IOException {
        String advertised = extensionParam("AUTH");
        if (advertised == null || advertised.isBlank()) {
            throw new SmtpException("server does not advertise AUTH");
        }
        Set<String> mechanisms = new HashSet<>(Arrays.asList(advertised.toUpperCase(Locale.ROOT).split(" ")));
        if (!mechanisms.contains("XOAUTH2")) {
            throw new SmtpException("server does not advertise XOAUTH2");
        }
        String token = "user=" + config.username()
                + "\u0001auth=Bearer " + config.oauthToken() + "\u0001\u0001";
        String encoded = Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
        SmtpResponse response = cmd("AUTH XOAUTH2 " + encoded);
        if (response.code() == 235) {
            authenticated = true;
            return;
        }
        if (response.code() == 334) {
            // The server sent a base64 error challenge; an empty line yields the final reply.
            response = cmd("");
        }
        throw new SmtpException("AUTH failed: " + response.message());
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

    private void writeDotStuffedStream(InputStream source) throws IOException {
        byte[] buffer = new byte[STREAM_CHUNK];
        ByteArrayOutputStream encoded = new ByteArrayOutputStream(STREAM_CHUNK + 16);
        boolean atLineStart = true;
        boolean sawAny = false;
        int read;
        while ((read = source.read(buffer)) != -1) {
            if (read == 0) {
                continue;
            }
            encoded.reset();
            for (int i = 0; i < read; i++) {
                byte b = buffer[i];
                if (atLineStart && b == '.') {
                    encoded.write('.');
                }
                encoded.write(b);
                atLineStart = b == '\n';
            }
            sawAny = true;
            writeBytes(encoded.toByteArray());
        }
        if (!sawAny || !atLineStart) {
            writeBytes(CRLF);
        }
        writeBytes(DOT_TERMINATOR);
    }

    private void writeBytes(byte[] data) throws IOException {
        writeBytes(data, 0, data.length);
    }

    private void writeBytes(byte[] data, int offset, int length) throws IOException {
        ensureActive();
        writing = true;
        try {
            out.write(data, offset, length);
            out.flush();
            lastProgressNanos = System.nanoTime();
        } catch (IOException e) {
            if (watchdogClosed && !cancelled) {
                throw new SocketTimeoutException("write stalled beyond the configured timeout");
            }
            throw e;
        } finally {
            writing = false;
        }
    }

    private String readLine() throws IOException {
        ensureActive();
        applyReadTimeout();
        try {
            String line = in.readLine();
            lastProgressNanos = System.nanoTime();
            if (line == null && (cancelled || watchdogClosed)) {
                throw new SmtpException("operation cancelled");
            }
            return line;
        } catch (IOException e) {
            if (cancelled) {
                throw new SmtpException("operation cancelled");
            }
            if (watchdogClosed) {
                throw new SocketTimeoutException("operation timed out");
            }
            throw e;
        }
    }

    private void applyReadTimeout() throws IOException {
        long readMillis = config.readTimeout() == null ? 0 : config.readTimeout().toMillis();
        long millis = readMillis;
        if (operationDeadlineNanos != Long.MAX_VALUE) {
            long remainingNanos = operationDeadlineNanos - System.nanoTime();
            long remainingMillis = Math.max(1L, remainingNanos / 1_000_000L);
            millis = millis <= 0 ? remainingMillis : Math.min(millis, remainingMillis);
        }
        if (millis > 0) {
            socket.setSoTimeout((int) Math.min(millis, Integer.MAX_VALUE));
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

    @FunctionalInterface
    private interface MessageTransfer {
        SmtpResponse send() throws IOException;
    }
}
