package com.mxraven.mail.notify;

import java.time.Duration;

/**
 * The transport seam between the notification client and NATS. The production
 * implementation wraps the official NATS client; tests substitute a fake.
 *
 * <p>The transport reconnects indefinitely on its own. {@link #isClosed()} and
 * {@link #terminalError()} report a connection that closed permanently.
 */
interface NotifyTransport extends AutoCloseable {

    /**
     * Opens one pull subscription over the listener's durable consumer.
     *
     * @return the subscription
     * @throws NotificationException when the subscription cannot be opened
     */
    Subscription subscribe();

    /**
     * Reports whether the connection is closed, whether by the caller or
     * because the transport gave up.
     *
     * @return {@code true} when the connection is closed
     */
    boolean isClosed();

    /**
     * Returns the terminal connection failure, when the connection closed on
     * its own.
     *
     * @return the error, or {@code null} when the caller closed the transport
     *         or it is still open
     */
    NotificationException terminalError();

    /** Closes the connection. It is safe to call more than once. */
    @Override
    void close();

    /** Opens a connected transport, resolving the listener's durable consumer. */
    interface Factory {
        /**
         * Connects and resolves the durable consumer.
         *
         * @param settings the validated connection settings
         * @return the connected transport
         * @throws NotificationException when the connection or consumer lookup fails
         */
        NotifyTransport open(NotifySettings settings);
    }

    /** One pull subscription over the durable consumer. */
    interface Subscription extends AutoCloseable {
        /**
         * Fetches the next raw message.
         *
         * @param timeout how long to wait for a message
         * @return the next message, or {@code null} when none arrived before
         *         the timeout
         * @throws InterruptedException when the calling thread is interrupted
         */
        RawMessage next(Duration timeout) throws InterruptedException;

        /** Stops the subscription. It is safe to call more than once. */
        @Override
        void close();
    }

    /** One raw delivery and its acknowledgement operations. */
    interface RawMessage {
        /**
         * The subject the message was published to.
         *
         * @return the subject
         */
        String subject();

        /**
         * The JSON body.
         *
         * @return the body bytes
         */
        byte[] body();

        /**
         * How often the server delivered this message, starting at 1.
         *
         * @return the delivery count
         */
        long deliveredCount();

        /** Acknowledges the message. It is not redelivered. */
        void ack();

        /**
         * Negatively acknowledges the message so the server redelivers it.
         *
         * @param delay the redelivery delay; zero redelivers immediately
         */
        void nakWithDelay(Duration delay);

        /** Tells the server never to redeliver the message. */
        void term();
    }
}
