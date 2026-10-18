package com.mxraven.mail.notify;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * One event yielded by a {@link NotificationIterator}.
 *
 * <p>Exactly one of {@link #ack()}, {@link #nak(Duration)}, or {@link #term()}
 * must be called for the event. Calling more than one throws
 * {@link NotificationException}. An event that is never settled is redelivered
 * after the consumer's acknowledgement timeout.
 */
public final class NotificationDelivery {
    private final NotificationEvent event;
    private final NotifyTransport.RawMessage message;
    private final AtomicBoolean settled = new AtomicBoolean();

    NotificationDelivery(NotificationEvent event, NotifyTransport.RawMessage message) {
        this.event = event;
        this.message = message;
    }

    /**
     * The decoded notification.
     *
     * @return the event
     */
    public NotificationEvent event() {
        return event;
    }

    /**
     * Acknowledges the event. It is not redelivered.
     *
     * @throws NotificationException when the event was already settled or the
     *                               acknowledgement cannot be sent
     */
    public void ack() {
        settle();
        message.ack();
    }

    /**
     * Negatively acknowledges the event so the server redelivers it
     * immediately.
     *
     * @throws NotificationException when the event was already settled or the
     *                               negative acknowledgement cannot be sent
     */
    public void nak() {
        nak(null);
    }

    /**
     * Negatively acknowledges the event so the server redelivers it.
     *
     * @param delay the redelivery delay; {@code null} or zero redelivers
     *              immediately
     * @throws NotificationException when the event was already settled, the
     *                               delay is negative, or the negative
     *                               acknowledgement cannot be sent
     */
    public void nak(Duration delay) {
        if (delay != null && delay.isNegative()) {
            throw new NotificationException("notify: invalid nak delay " + delay.toMillis() + "ms");
        }
        settle();
        message.nakWithDelay(delay == null ? Duration.ZERO : delay);
    }

    /**
     * Tells the server never to redeliver the event.
     *
     * <p>The NATS Java client does not transmit a termination reason; the
     * server records a bare termination.
     *
     * @throws NotificationException when the event was already settled or the
     *                               termination cannot be sent
     */
    public void term() {
        settle();
        message.term();
    }

    private void settle() {
        if (!settled.compareAndSet(false, true)) {
            throw new NotificationException("notify: event is already settled");
        }
    }
}
