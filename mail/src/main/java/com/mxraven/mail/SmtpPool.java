package com.mxraven.mail;

import com.mxraven.mail.model.Envelope;
import com.mxraven.mail.model.Mail;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * A bounded, thread-safe pool of authenticated SMTP connections.
 *
 * <p>A pool lazily connects up to its maximum size and reuses idle connections
 * for later sends. It is safe for concurrent use: each send borrows one
 * connection for the duration of the transaction. Use it instead of
 * {@link SmtpClient} when multiple threads submit mail.
 *
 * <p>A connection is discarded rather than reused when a send throws, because
 * the failed exchange may have left unread replies on the wire. Server
 * rejections (a non-success {@link SendResult}) leave the connection usable.
 *
 * <p>Connections are not validated before reuse. If the server closes an idle
 * connection, the next send on it fails with an {@link IOException}.
 *
 * <pre>
 * SmtpPool pool = new SmtpPool(config, 5);
 * try {
 *     SendResult result = pool.send(mail);
 * } finally {
 *     pool.close();
 * }
 * </pre>
 */
public final class SmtpPool implements AutoCloseable {
    /** Default maximum number of connections. */
    public static final int DEFAULT_MAX_SIZE = 5;

    private final SmtpConfig config;
    private final int maxSize;
    private final BlockingQueue<SmtpClient> idle = new LinkedBlockingQueue<>();
    private final Semaphore slots;
    private volatile boolean closed;

    /**
     * Creates a pool with the {@link #DEFAULT_MAX_SIZE default size}.
     *
     * @param config the connection configuration used for every connection
     */
    public SmtpPool(SmtpConfig config) {
        this(config, DEFAULT_MAX_SIZE);
    }

    /**
     * Creates a pool with the given maximum number of connections.
     *
     * @param config  the connection configuration used for every connection
     * @param maxSize the maximum number of open connections, which must be positive
     * @throws IllegalArgumentException when {@code config} is {@code null} or
     *                                  {@code maxSize} is not positive
     */
    public SmtpPool(SmtpConfig config, int maxSize) {
        if (config == null) {
            throw new IllegalArgumentException("config is required");
        }
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize must be positive");
        }
        this.config = config;
        this.maxSize = maxSize;
        this.slots = new Semaphore(maxSize);
    }

    /**
     * Sends a built message on a pooled connection.
     *
     * @param mail the message to send
     * @return the send result
     * @throws IOException   when the SMTP exchange fails or the wait is interrupted
     * @throws SmtpException when the pool is closed
     */
    public SendResult send(Mail mail) throws IOException {
        if (mail == null) {
            throw new IllegalArgumentException("mail is required");
        }
        return withClient(client -> client.send(mail));
    }

    /**
     * Sends a prebuilt RFC 5322 message on a pooled connection.
     *
     * @param envelope   the SMTP envelope
     * @param rawMessage the raw RFC 5322 message bytes
     * @return the send result
     * @throws IOException   when the SMTP exchange fails or the wait is interrupted
     * @throws SmtpException when the pool is closed
     */
    public SendResult sendRaw(Envelope envelope, byte[] rawMessage) throws IOException {
        return withClient(client -> client.sendRaw(envelope, rawMessage));
    }

    /**
     * Returns the configured maximum number of connections.
     *
     * @return the maximum pool size
     */
    public int maxSize() {
        return maxSize;
    }

    /**
     * Closes idle connections and marks the pool closed. Connections currently in
     * use are allowed to finish but are not returned to the pool.
     */
    @Override
    public void close() {
        closed = true;
        SmtpClient client;
        while ((client = idle.poll()) != null) {
            try {
                client.close();
            } catch (IOException ignored) {
                // Best-effort cleanup during close.
            }
        }
    }

    private SendResult withClient(ClientTask task) throws IOException {
        if (closed) {
            throw new SmtpException("pool is closed");
        }
        try {
            slots.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("interrupted while waiting for an SMTP connection", e);
        }
        SmtpClient client = null;
        try {
            client = idle.poll();
            if (client == null) {
                client = SmtpClient.connect(config);
            }
            SendResult result = task.run(client);
            returnClient(client);
            client = null;
            return result;
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException ignored) {
                    // The connection is already unusable; closing is best effort.
                }
            }
            slots.release();
        }
    }

    private void returnClient(SmtpClient client) {
        if (closed || !idle.offer(client)) {
            try {
                client.close();
            } catch (IOException ignored) {
                // Best-effort cleanup of an unpooled connection.
            }
        }
    }

    @FunctionalInterface
    private interface ClientTask {
        SendResult run(SmtpClient client) throws IOException;
    }
}
