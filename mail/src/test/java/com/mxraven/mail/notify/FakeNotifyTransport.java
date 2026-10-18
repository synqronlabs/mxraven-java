package com.mxraven.mail.notify;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * An in-process fake notification transport with a scripted event queue and
 * recorded settlements, mirroring the reference SDK's fake NATS fixture.
 */
final class FakeNotifyTransport implements NotifyTransport {

    /** An injectable factory that records the settings it was opened with. */
    static final class Factory implements NotifyTransport.Factory {
        NotifySettings settings;
        boolean consumerNotFound;
        FakeNotifyTransport transport;

        @Override
        public NotifyTransport open(NotifySettings settings) {
            this.settings = settings;
            if (consumerNotFound) {
                throw new NotificationException(
                        "notify: resolve consumer \"" + settings.consumer() + "\"");
            }
            transport = new FakeNotifyTransport();
            return transport;
        }
    }

    private final BlockingQueue<FakeRawMessage> queue = new LinkedBlockingQueue<>();
    private final List<String> settlements = new CopyOnWriteArrayList<>();
    private volatile boolean closed;

    /** Enqueues one event with a first delivery count. */
    void enqueue(String subject, String body) {
        enqueue(subject, body, 1);
    }

    /** Enqueues one event with an explicit delivery count. */
    void enqueue(String subject, String body, long deliveredCount) {
        queue.add(new FakeRawMessage(subject, body, deliveredCount));
    }

    /** The settlements recorded so far: {@code ack}, {@code nak:<millis>}, {@code term}. */
    List<String> settlements() {
        return List.copyOf(settlements);
    }

    @Override
    public Subscription subscribe() {
        if (closed) {
            throw new NotificationException("notify: client is closed");
        }
        return new FakeSubscription();
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public NotificationException terminalError() {
        return null;
    }

    @Override
    public void close() {
        closed = true;
    }

    private final class FakeSubscription implements Subscription {
        @Override
        public RawMessage next(Duration timeout) throws InterruptedException {
            if (closed) {
                return null;
            }
            return queue.poll(timeout.toMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public void close() {
            // Nothing to release.
        }
    }

    private final class FakeRawMessage implements RawMessage {
        private final String subject;
        private final byte[] body;
        private final long deliveredCount;

        FakeRawMessage(String subject, String body, long deliveredCount) {
            this.subject = subject;
            this.body = body.getBytes(StandardCharsets.UTF_8);
            this.deliveredCount = deliveredCount;
        }

        @Override
        public String subject() {
            return subject;
        }

        @Override
        public byte[] body() {
            return body;
        }

        @Override
        public long deliveredCount() {
            return deliveredCount;
        }

        @Override
        public void ack() {
            settlements.add("ack");
        }

        @Override
        public void nakWithDelay(Duration delay) {
            settlements.add("nak:" + (delay == null ? 0 : delay.toMillis()));
        }

        @Override
        public void term() {
            settlements.add("term");
        }
    }
}
