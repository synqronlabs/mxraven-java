package com.mxraven.mail;

import com.mxraven.mail.model.Envelope;
import com.mxraven.mail.model.Mail;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A bounded, thread-safe pool of authenticated SMTP connections.
 *
 * <p>A pool lazily connects up to its maximum size and reuses idle connections
 * for later sends. It is safe for concurrent use: each send borrows one
 * connection for the duration of the transaction. Use it instead of
 * {@link SmtpClient} when multiple threads submit mail.
 *
 * <p>Idle connections are health-checked with a short {@code NOOP} before reuse,
 * evicted after {@link #DEFAULT_MAX_IDLE} idle, and retired after
 * {@link #DEFAULT_MAX_LIFETIME} regardless of use. This avoids the common
 * failure where the server closes a stale connection and the next send fails.
 *
 * <p>A connection is discarded rather than reused when a send throws, because
 * the failed exchange may have left unread replies on the wire. Server
 * rejections (a non-success {@link SendResult}) leave the connection usable.
 * Sends are not retried, because a failed transaction may already have been
 * partially delivered; callers decide whether to resend.
 *
 * <pre>{@code
 * try (SmtpPool pool = new SmtpPool(config, 5)) {
 *     SendResult result = pool.send(mail);
 * }
 * }</pre>
 */
public final class SmtpPool implements AutoCloseable {
    /** Default maximum number of connections. */
    public static final int DEFAULT_MAX_SIZE = 5;
    /** Default time an idle connection is kept before being closed. */
    public static final Duration DEFAULT_MAX_IDLE = Duration.ofMinutes(5);
    /** Default total lifetime of a connection. */
    public static final Duration DEFAULT_MAX_LIFETIME = Duration.ofMinutes(30);

    private static final long PROBE_IDLE_MILLIS = 1_000L;
    private static final long PROBE_TIMEOUT_MILLIS = 5_000L;

    private final SmtpConfig config;
    private final int maxSize;
    private final long maxIdleNanos;
    private final long maxLifetimeNanos;
    private final Semaphore slots;
    private final Object idleLock = new Object();
    private final ArrayDeque<PooledClient> idle = new ArrayDeque<>();
    private final AtomicInteger active = new AtomicInteger();
    private final Object drainLock = new Object();
    private volatile boolean closed;

    /**
     * Creates a pool with the {@link #DEFAULT_MAX_SIZE default size} and lifetimes.
     *
     * @param config the connection configuration used for every connection
     */
    public SmtpPool(SmtpConfig config) {
        this(config, DEFAULT_MAX_SIZE, DEFAULT_MAX_IDLE, DEFAULT_MAX_LIFETIME);
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
        this(config, maxSize, DEFAULT_MAX_IDLE, DEFAULT_MAX_LIFETIME);
    }

    /**
     * Creates a pool with explicit idle and lifetime limits.
     *
     * @param config      the connection configuration
     * @param maxSize     the maximum number of open connections; must be positive
     * @param maxIdle     how long an idle connection is kept; zero disables idle eviction
     * @param maxLifetime the total lifetime of a connection; zero disables retirement
     * @throws IllegalArgumentException when an argument is invalid
     */
    public SmtpPool(SmtpConfig config, int maxSize, Duration maxIdle, Duration maxLifetime) {
        if (config == null) {
            throw new IllegalArgumentException("config is required");
        }
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize must be positive");
        }
        if (maxIdle == null || maxLifetime == null) {
            throw new IllegalArgumentException("maxIdle and maxLifetime are required");
        }
        if (maxIdle.isNegative() || maxLifetime.isNegative()) {
            throw new IllegalArgumentException("maxIdle and maxLifetime must not be negative");
        }
        this.config = config;
        this.maxSize = maxSize;
        this.maxIdleNanos = maxIdle.toNanos();
        this.maxLifetimeNanos = maxLifetime.toNanos();
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
        return send(mail, SendOptions.defaults());
    }

    /**
     * Sends a built message with per-send options on a pooled connection.
     *
     * @param mail    the message to send
     * @param options the deadline and/or cancellation, or {@code null} for none
     * @return the send result
     * @throws IOException   when the SMTP exchange fails
     * @throws SmtpException when the pool is closed
     */
    public SendResult send(Mail mail, SendOptions options) throws IOException {
        if (mail == null) {
            throw new IllegalArgumentException("mail is required");
        }
        return withClient(client -> client.send(mail, options));
    }

    /**
     * Sends a prebuilt RFC 5322 message on a pooled connection.
     *
     * @param envelope   the SMTP envelope
     * @param rawMessage the raw RFC 5322 message bytes
     * @return the send result
     * @throws IOException   when the SMTP exchange fails
     * @throws SmtpException when the pool is closed
     */
    public SendResult sendRaw(Envelope envelope, byte[] rawMessage) throws IOException {
        return sendRaw(envelope, rawMessage, SendOptions.defaults());
    }

    /**
     * Sends a prebuilt message with per-send options on a pooled connection.
     *
     * @param envelope   the SMTP envelope
     * @param rawMessage the raw RFC 5322 message bytes
     * @param options    the deadline and/or cancellation, or {@code null} for none
     * @return the send result
     * @throws IOException   when the SMTP exchange fails
     * @throws SmtpException when the pool is closed
     */
    public SendResult sendRaw(Envelope envelope, byte[] rawMessage, SendOptions options) throws IOException {
        return withClient(client -> client.sendRaw(envelope, rawMessage, options));
    }

    /**
     * Streams a prebuilt RFC 5322 message on a pooled connection.
     *
     * @param envelope   the SMTP envelope
     * @param rawMessage the raw RFC 5322 message stream, not closed
     * @return the send result
     * @throws IOException   when the SMTP exchange fails
     * @throws SmtpException when the pool is closed
     */
    public SendResult sendRaw(Envelope envelope, InputStream rawMessage) throws IOException {
        return sendRaw(envelope, rawMessage, SendOptions.defaults());
    }

    /**
     * Streams a prebuilt message with per-send options on a pooled connection.
     *
     * @param envelope   the SMTP envelope
     * @param rawMessage the raw RFC 5322 message stream, not closed
     * @param options    the deadline and/or cancellation, or {@code null} for none
     * @return the send result
     * @throws IOException   when the SMTP exchange fails
     * @throws SmtpException when the pool is closed
     */
    public SendResult sendRaw(Envelope envelope, InputStream rawMessage, SendOptions options)
            throws IOException {
        return withClient(client -> client.sendRaw(envelope, rawMessage, options));
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
     * Returns the number of connections currently borrowed.
     *
     * @return the number of in-flight sends
     */
    public int activeCount() {
        return active.get();
    }

    /**
     * Closes idle connections and marks the pool closed. Connections currently in
     * use are allowed to finish; they are closed when returned instead of being
     * pooled. Equivalent to {@code close(Duration.ZERO)}.
     */
    @Override
    public void close() {
        close(Duration.ZERO);
    }

    /**
     * Marks the pool closed and waits up to {@code gracePeriod} for in-flight
     * sends to finish, then closes idle connections. Connections that finish
     * after the grace period are closed on return.
     *
     * @param gracePeriod how long to wait for in-flight sends; must not be negative
     * @throws IllegalArgumentException when {@code gracePeriod} is {@code null} or negative
     */
    public void close(Duration gracePeriod) {
        if (gracePeriod == null || gracePeriod.isNegative()) {
            throw new IllegalArgumentException("gracePeriod must not be negative");
        }
        closed = true;
        long deadline = System.nanoTime() + gracePeriod.toNanos();
        synchronized (drainLock) {
            while (active.get() > 0) {
                long remaining = deadline - System.nanoTime();
                if (remaining <= 0) {
                    break;
                }
                try {
                    drainLock.wait(Math.max(1L, remaining / 1_000_000L));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        closeIdle();
    }

    private void closeIdle() {
        PooledClient pooled;
        while ((pooled = pollIdle()) != null) {
            closeQuietly(pooled.client);
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
        active.incrementAndGet();
        SmtpClient client = null;
        try {
            client = borrow();
            SendResult result = task.run(client);
            recycle(client);
            client = null;
            return result;
        } finally {
            if (client != null) {
                closeQuietly(client);
            }
            if (active.decrementAndGet() == 0) {
                synchronized (drainLock) {
                    drainLock.notifyAll();
                }
            }
            slots.release();
        }
    }

    private SmtpClient borrow() throws IOException {
        long now = System.nanoTime();
        PooledClient pooled = pollIdle();
        while (pooled != null) {
            if (expired(pooled, now)) {
                closeQuietly(pooled.client);
            } else if (needsProbe(pooled, now) && !pooled.client.probe(PROBE_TIMEOUT_MILLIS)) {
                // Discard the dead connection and try the next idle one.
            } else {
                return pooled.client;
            }
            pooled = pollIdle();
        }
        if (closed) {
            throw new SmtpException("pool is closed");
        }
        return SmtpClient.connect(config);
    }

    private boolean expired(PooledClient pooled, long now) {
        if (maxLifetimeNanos > 0 && now - pooled.createdAtNanos > maxLifetimeNanos) {
            return true;
        }
        return maxIdleNanos > 0 && now - pooled.lastUsedNanos > maxIdleNanos;
    }

    private boolean needsProbe(PooledClient pooled, long now) {
        return now - pooled.lastUsedNanos >= PROBE_IDLE_MILLIS * 1_000_000L;
    }

    private PooledClient pollIdle() {
        synchronized (idleLock) {
            return idle.pollFirst();
        }
    }

    private void recycle(SmtpClient client) {
        if (closed) {
            closeQuietly(client);
            return;
        }
        long now = System.nanoTime();
        PooledClient pooled = new PooledClient(client, now, now);
        synchronized (idleLock) {
            if (closed) {
                closeQuietly(client);
            } else {
                idle.addLast(pooled);
            }
        }
    }

    private static void closeQuietly(SmtpClient client) {
        try {
            client.close();
        } catch (IOException ignored) {
            // The connection is already unusable; closing is best effort.
        }
    }

    @FunctionalInterface
    private interface ClientTask {
        SendResult run(SmtpClient client) throws IOException;
    }

    private static final class PooledClient {
        private final SmtpClient client;
        private final long createdAtNanos;
        private final long lastUsedNanos;

        PooledClient(SmtpClient client, long createdAtNanos, long lastUsedNanos) {
            this.client = client;
            this.createdAtNanos = createdAtNanos;
            this.lastUsedNanos = lastUsedNanos;
        }
    }
}
