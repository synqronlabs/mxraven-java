package com.mxraven.mail.notify;

import io.nats.client.Connection;
import io.nats.client.ConnectionListener;
import io.nats.client.JetStream;
import io.nats.client.JetStreamApiException;
import io.nats.client.JetStreamSubscription;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Options;
import io.nats.client.PullSubscribeOptions;
import io.nats.client.api.ConsumerConfiguration;
import io.nats.client.api.ConsumerInfo;
import io.nats.client.impl.NatsJetStreamMetaData;

import java.io.IOException;
import java.time.Duration;
import java.util.function.Consumer;

/**
 * The jnats-backed notification transport.
 *
 * <p>The stream and consumer are managed by the control plane; this transport
 * only binds to the listener's durable consumer and pulls from it.
 */
final class JnatsNotifyTransport implements NotifyTransport {

    static final Factory FACTORY = new Factory() {
        @Override
        public NotifyTransport open(NotifySettings settings) {
            return JnatsNotifyTransport.open(settings);
        }
    };

    /** The stream that carries client notification events. */
    private static final String STREAM_NAME = "CLIENT_NOTIFY";

    /** The client name the notification tier sees. */
    private static final String CLIENT_NAME = "mxraven-notify";

    private final Connection connection;
    private final JetStream jetStream;
    private final NotifySettings settings;
    private final String consumer;
    private final String filterSubject;
    private volatile boolean userClosed;

    private JnatsNotifyTransport(Connection connection, JetStream jetStream, NotifySettings settings,
                                 String consumer, String filterSubject) {
        this.connection = connection;
        this.jetStream = jetStream;
        this.settings = settings;
        this.consumer = consumer;
        this.filterSubject = filterSubject;
        connection.addConnectionListener(new ConnectionListener() {
            @Override
            public void connectionEvent(Connection conn, Events type) {
                JnatsNotifyTransport.this.onConnectionEvent(conn, type);
            }
        });
    }

    /**
     * Connects to the notification tier and resolves the listener's durable
     * consumer.
     *
     * @param settings the validated connection settings
     * @return the connected transport
     * @throws NotificationException when the connection or consumer lookup fails
     */
    static NotifyTransport open(NotifySettings settings) {
        Options.Builder builder = Options.builder()
                .server(settings.url())
                .userInfo(settings.username(), settings.secret())
                .connectionName(CLIENT_NAME)
                // The callout grants exactly this per-listener inbox prefix.
                .inboxPrefix("_INBOX." + settings.listener())
                .maxReconnects(-1)
                .connectionTimeout(settings.connectTimeout());
        if (settings.sslContext() != null) {
            builder.sslContext(settings.sslContext());
        }

        Connection connection;
        try {
            connection = Nats.connect(builder.build());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new NotificationException("notify: connect", e);
        } catch (IOException e) {
            throw new NotificationException("notify: connect", e);
        }

        try {
            JetStream jetStream = connection.jetStream();
            ConsumerInfo info = connection.jetStreamManagement()
                    .getConsumerInfo(STREAM_NAME, settings.consumer());
            ConsumerConfiguration config = info.getConsumerConfiguration();
            return new JnatsNotifyTransport(connection, jetStream, settings, settings.consumer(),
                    config.getFilterSubject());
        } catch (IOException | JetStreamApiException e) {
            closeQuietly(connection);
            throw new NotificationException(
                    "notify: resolve consumer \"" + settings.consumer() + "\"", e);
        }
    }

    @Override
    public Subscription subscribe() {
        if (isClosed()) {
            throw new NotificationException("notify: client is closed");
        }
        try {
            JetStreamSubscription subscription = jetStream.subscribe(filterSubject,
                    PullSubscribeOptions.builder().stream(STREAM_NAME).durable(consumer).build());
            return new JnatsSubscription(subscription);
        } catch (IOException | JetStreamApiException e) {
            throw new NotificationException("notify: start consume", e);
        }
    }

    @Override
    public boolean isClosed() {
        return userClosed || connection.getStatus() == Connection.Status.CLOSED;
    }

    @Override
    public NotificationException terminalError() {
        if (userClosed || connection.getStatus() != Connection.Status.CLOSED) {
            return null;
        }
        String last = connection.getLastError();
        return new NotificationException("notify: connection closed"
                + (last == null || last.isEmpty() ? "" : ": " + last));
    }

    @Override
    public void close() {
        if (userClosed) {
            return;
        }
        userClosed = true;
        try {
            connection.close();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new NotificationException("notify: close", e);
        }
    }

    /** Forwards non-fatal connection failures to the configured handler. */
    private void onConnectionEvent(Connection conn, ConnectionListener.Events type) {
        if (type == ConnectionListener.Events.DISCONNECTED) {
            report(new NotificationException("notify: connection disconnected"));
        }
    }

    private void report(NotificationException error) {
        Consumer<NotificationException> handler = settings.onError();
        if (handler == null) {
            return;
        }
        try {
            handler.accept(error);
        } catch (RuntimeException ignored) {
            // The callback contract says not to throw. Keeping the transport
            // alive is more useful than tearing it down from an error reporter.
        }
    }

    private static void closeQuietly(Connection connection) {
        try {
            connection.close();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** One pull subscription over the durable consumer. */
    private static final class JnatsSubscription implements Subscription {
        private final JetStreamSubscription subscription;
        private volatile boolean closed;

        JnatsSubscription(JetStreamSubscription subscription) {
            this.subscription = subscription;
        }

        @Override
        public RawMessage next(Duration timeout) throws InterruptedException {
            Message message;
            try {
                message = subscription.nextMessage(timeout);
            } catch (IllegalStateException e) {
                // A status message or a closed connection ends the pull. The
                // client loop converts this to a clean stop (user close) or a
                // terminal failure (a consumer that no longer exists).
                throw new NotificationException("notify: consume", e);
            }
            return message == null ? null : new JnatsRawMessage(message);
        }

        @Override
        public void close() {
            if (closed) {
                return;
            }
            closed = true;
            try {
                subscription.unsubscribe();
            } catch (RuntimeException ignored) {
                // The connection may already be closed; there is nothing left
                // to unsubscribe.
            }
        }
    }

    /** One raw delivery and its acknowledgement operations. */
    private static final class JnatsRawMessage implements RawMessage {
        private final Message message;

        JnatsRawMessage(Message message) {
            this.message = message;
        }

        @Override
        public String subject() {
            return message.getSubject();
        }

        @Override
        public byte[] body() {
            return message.getData();
        }

        @Override
        public long deliveredCount() {
            NatsJetStreamMetaData metaData = message.metaData();
            return metaData == null ? 1 : metaData.deliveredCount();
        }

        @Override
        public void ack() {
            try {
                message.ack();
            } catch (RuntimeException e) {
                throw new NotificationException("notify: acknowledge event", e);
            }
        }

        @Override
        public void nakWithDelay(Duration delay) {
            try {
                if (delay == null || delay.equals(Duration.ZERO)) {
                    message.nak();
                } else {
                    message.nakWithDelay(delay);
                }
            } catch (RuntimeException e) {
                throw new NotificationException("notify: negate event", e);
            }
        }

        @Override
        public void term() {
            try {
                message.term();
            } catch (RuntimeException e) {
                throw new NotificationException("notify: terminate event", e);
            }
        }
    }
}
