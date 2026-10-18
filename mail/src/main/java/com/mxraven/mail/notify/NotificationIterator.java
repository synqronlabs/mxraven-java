package com.mxraven.mail.notify;

import com.mxraven.mail.Cancellation;

import java.time.Duration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates one listener's notification events without automatic
 * acknowledgement.
 *
 * <p>The caller acknowledges each event with {@link NotificationDelivery#ack()},
 * {@link NotificationDelivery#nak(Duration)}, or
 * {@link NotificationDelivery#term()}. Stopping iteration leaves unacknowledged
 * events for redelivery after the consumer's acknowledgement timeout.
 *
 * <p>Iteration stops when the client closes, the iterator closes, the
 * configured cancellation fires, or the consumer is permanently lost. An
 * iterator must not be used from more than one thread at a time. Use
 * {@link #close()} for explicit cleanup; the iterator is also safe as a
 * for-each source.
 */
public final class NotificationIterator implements Iterator<NotificationDelivery>, AutoCloseable {
    private static final Duration POLL_TIMEOUT = Duration.ofMillis(500);

    private final NotificationClient client;
    private final Cancellation cancellation;
    private final boolean startedClosed;
    private NotifyTransport.Subscription subscription;
    private NotificationDelivery pending;
    private boolean done;

    NotificationIterator(NotificationClient client, Cancellation cancellation) {
        this.client = client;
        this.cancellation = cancellation;
        this.startedClosed = client.isClosed();
    }

    @Override
    public boolean hasNext() {
        if (pending != null) {
            return true;
        }
        if (startedClosed) {
            throw new NotificationException("notify: client is closed");
        }
        if (done || isStopped()) {
            finish();
            return false;
        }
        for (;;) {
            NotifyTransport.RawMessage raw = nextRaw();
            if (raw == null) {
                if (isStopped()) {
                    finish();
                    return false;
                }
                continue;
            }

            NotificationEvent event;
            try {
                event = NotifyDecoder.decode(raw.subject(), raw.body());
            } catch (NotificationDecodeException e) {
                try {
                    raw.term();
                } catch (NotificationException termError) {
                    client.report(termError);
                }
                if (client.hasErrorHandler()) {
                    client.report(e);
                    continue;
                }
                finish();
                throw e;
            }

            pending = new NotificationDelivery(event, raw);
            return true;
        }
    }

    @Override
    public NotificationDelivery next() {
        if (!hasNext()) {
            throw new NoSuchElementException("notification iterator is stopped");
        }
        NotificationDelivery delivery = pending;
        pending = null;
        return delivery;
    }

    @Override
    public void close() {
        finish();
    }

    /** Fetches the next raw message, opening the pull on first use. */
    private NotifyTransport.RawMessage nextRaw() {
        if (subscription == null) {
            if (done) {
                return null;
            }
            subscription = client.subscribe();
        }
        try {
            return subscription.next(POLL_TIMEOUT);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            finish();
            return null;
        } catch (NotificationException e) {
            if (isStopped()) {
                finish();
                return null;
            }
            throw e;
        }
    }

    private boolean isStopped() {
        return done || client.isClosed() || (cancellation != null && cancellation.isCancelled());
    }

    private void finish() {
        done = true;
        if (subscription != null) {
            subscription.close();
            subscription = null;
        }
    }
}
