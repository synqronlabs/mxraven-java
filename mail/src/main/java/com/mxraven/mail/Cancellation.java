package com.mxraven.mail;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A cooperative cancellation signal for a single blocking call.
 *
 * <p>Pass one to a send method through {@link SendOptions#cancellation()} and
 * call {@link #cancel()} from another thread to abort it. If the client is
 * between commands, the operation stops and the connection stays usable. If it
 * is blocked in a read or write, the connection is closed to unblock it, and
 * that {@link SmtpClient} must not be reused. Unlike {@link SmtpClient#cancel()},
 * which always tears down the connection, a {@code Cancellation} targets a single
 * operation.
 *
 * <p>This type is thread-safe.
 */
public final class Cancellation {
    private final AtomicBoolean cancelled = new AtomicBoolean();

    private Cancellation() {
    }

    /**
     * Creates a fresh, uncancelled signal.
     *
     * @return a new cancellation
     */
    public static Cancellation create() {
        return new Cancellation();
    }

    /**
     * Requests cancellation. Idempotent and safe to call from any thread.
     */
    public void cancel() {
        cancelled.set(true);
    }

    /**
     * Reports whether cancellation was requested.
     *
     * @return {@code true} once {@link #cancel()} has been called
     */
    public boolean isCancelled() {
        return cancelled.get();
    }
}
