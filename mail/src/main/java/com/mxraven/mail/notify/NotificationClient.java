package com.mxraven.mail.notify;

import com.mxraven.mail.Cancellation;

import java.time.Duration;

/**
 * Consumes one listener's mxRaven client notification events over NATS
 * JetStream.
 *
 * <p>Notifications are off by default. Enable them for the listener with the
 * mxRaven administrative API first; that creates the durable consumer this
 * client reads from.
 *
 * <p>The connection authenticates with the listener's ordinary API key and
 * only grants access to that listener's consumer, so the key prefix and secret
 * are the same values used for SMTP submission.
 *
 * <p>A client is safe for concurrent use. A {@link #consume} call and an
 * {@link #events()} iterator share the durable consumer with any other client
 * using the same API key: events are distributed between them, not duplicated
 * to each.
 *
 * <p>The stream keeps events for 24 hours from creation regardless of
 * acknowledgement. Acknowledging stops redelivery; it does not remove the
 * event. A client that is offline longer than the retention window cannot
 * recover the gap.
 *
 * <pre>
 * try (NotificationClient client = NotificationClient.connect(
 *         NotificationClientOptions.builder()
 *                 .username("mxr_tx_ab12cd34ef56")
 *                 .secret(System.getenv("MXRAVEN_SECRET"))
 *                 .listener(listenerId)
 *                 .build())) {
 *     client.consume(new NotificationCallbacks() {
 *         &#64;Override
 *         public HandlerResult onAccepted(InboundEmail email) {
 *             System.out.println("accepted " + email.taskId());
 *             return HandlerResult.CONTINUE;
 *         }
 *     });
 * }
 * </pre>
 */
public final class NotificationClient implements AutoCloseable {
    /**
     * The default client notification tier URL. Override it through
     * {@link NotificationClientOptions.Builder#url(String)} for staging or
     * self-hosted tiers.
     */
    public static final String DEFAULT_URL = "wss://nats.mxraven.com";

    private static final Duration POLL_TIMEOUT = Duration.ofMillis(500);

    /** The base redelivery delay for failed handlers. */
    static final long RETRY_BASE_MILLIS = 1_000;

    /** The maximum redelivery delay for failed handlers. */
    static final long RETRY_MAX_MILLIS = 30_000;

    private final NotifyTransport transport;
    private final NotifySettings settings;
    private volatile boolean userClosed;

    NotificationClient(NotifyTransport transport, NotifySettings settings) {
        this.transport = transport;
        this.settings = settings;
    }

    /**
     * Connects to the notification tier and resolves the listener's durable
     * consumer.
     *
     * <p>The initial connection and the consumer lookup fail here rather than
     * on the first event. After a successful connection the transport
     * reconnects indefinitely when the tier is unreachable, and the consumer
     * resumes from its acknowledgement floor.
     *
     * @param options the URL, listener API key, and listener identifier
     * @return a connected client
     * @throws NotificationException when an option is invalid, the server
     *                               rejects the API key, or the listener's
     *                               consumer does not exist
     */
    public static NotificationClient connect(NotificationClientOptions options) {
        return connect(options, JnatsNotifyTransport.FACTORY);
    }

    /** Connects through an injected transport factory. */
    static NotificationClient connect(NotificationClientOptions options, NotifyTransport.Factory factory) {
        NotifySettings settings = NotifySettings.normalize(options);
        return new NotificationClient(factory.open(settings), settings);
    }

    /**
     * Consumes events until the handler stops, the client closes, or the
     * calling thread is interrupted.
     *
     * <p>A handler that returns {@link HandlerResult#CONTINUE} acknowledges the
     * event. A handler that throws leaves it unacknowledged, and the server
     * redelivers it after a delay that doubles with each attempt up to 30
     * seconds. Events are handled one at a time, so a slow handler applies
     * backpressure to the pull.
     *
     * <p>An undecodable event is terminated and this method throws
     * {@link NotificationDecodeException}, unless an error handler is
     * configured, in which case the handler receives the error and consumption
     * continues with the next event. A permanently lost consumer or connection
     * throws {@link NotificationException}.
     *
     * @param handler the handler that processes one event at a time
     * @throws NotificationException when consumption fails or the client is closed
     */
    public void consume(NotificationHandler handler) {
        consume(handler, null);
    }

    /**
     * Consumes events until the handler stops, the client closes, or the
     * cancellation fires.
     *
     * @param handler      the handler that processes one event at a time
     * @param cancellation a cancellation signal, or {@code null}
     * @throws NotificationException when consumption fails or the client is closed
     */
    public void consume(NotificationHandler handler, Cancellation cancellation) {
        if (handler == null) {
            throw new NotificationException("notify: handler must not be null");
        }
        if (isClosed()) {
            throw new NotificationException("notify: client is closed");
        }
        if (isCancelled(cancellation)) {
            return;
        }

        try (NotifyTransport.Subscription subscription = transport.subscribe()) {
            while (true) {
                if (isCancelled(cancellation) || isClosed()) {
                    break;
                }

                NotifyTransport.RawMessage raw;
                try {
                    raw = subscription.next(POLL_TIMEOUT);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                if (raw == null) {
                    continue;
                }

                NotificationEvent event;
                try {
                    event = NotifyDecoder.decode(raw.subject(), raw.body());
                } catch (NotificationDecodeException e) {
                    try {
                        raw.term();
                    } catch (NotificationException termError) {
                        report(termError);
                    }
                    if (hasErrorHandler()) {
                        report(e);
                        continue;
                    }
                    throw e;
                }

                HandlerResult result;
                try {
                    result = handler.handle(event);
                } catch (Exception e) {
                    try {
                        raw.nakWithDelay(retryDelay(raw.deliveredCount()));
                    } catch (NotificationException nakError) {
                        report(nakError);
                    }
                    continue;
                }

                try {
                    raw.ack();
                } catch (NotificationException ackError) {
                    report(ackError);
                }
                if (result == HandlerResult.STOP) {
                    break;
                }
            }
        } catch (NotificationException e) {
            if (isCancelled(cancellation) || userClosed) {
                return;
            }
            throw e;
        }

        if (!isCancelled(cancellation) && !userClosed) {
            NotificationException terminal = transport.terminalError();
            if (terminal != null) {
                throw terminal;
            }
        }
    }

    /**
     * Iterates events without automatic acknowledgement.
     *
     * @return an iterator over the listener's events
     * @throws NotificationException when the client is closed
     */
    public NotificationIterator events() {
        return events(null);
    }

    /**
     * Iterates events, stopping when the cancellation fires.
     *
     * @param cancellation a cancellation signal, or {@code null}
     * @return an iterator over the listener's events
     * @throws NotificationException when the client is closed
     */
    public NotificationIterator events(Cancellation cancellation) {
        return new NotificationIterator(this, cancellation);
    }

    /**
     * Releases the client's connection.
     *
     * <p>Active {@code consume} calls and iterators stop, and events they did
     * not acknowledge are redelivered later. Close is safe to call more than
     * once.
     *
     * @throws NotificationException when the connection cannot be closed cleanly
     */
    @Override
    public void close() {
        if (userClosed) {
            return;
        }
        userClosed = true;
        transport.close();
    }

    boolean isClosed() {
        return userClosed || transport.isClosed();
    }

    boolean hasErrorHandler() {
        return settings.onError() != null;
    }

    void report(NotificationException error) {
        if (settings.onError() == null) {
            return;
        }
        try {
            settings.onError().accept(error);
        } catch (RuntimeException ignored) {
            // The callback contract says not to throw.
        }
    }

    NotifyTransport.Subscription subscribe() {
        if (isClosed()) {
            throw new NotificationException("notify: client is closed");
        }
        return transport.subscribe();
    }

    /** Returns the redelivery delay for a failed handler, capped at 30 seconds. */
    static Duration retryDelay(long deliveredCount) {
        long delay = RETRY_BASE_MILLIS;
        if (deliveredCount > 1) {
            for (long attempt = 1; attempt < deliveredCount && delay < RETRY_MAX_MILLIS; attempt++) {
                delay *= 2;
            }
        }
        return Duration.ofMillis(Math.min(delay, RETRY_MAX_MILLIS));
    }

    private static boolean isCancelled(Cancellation cancellation) {
        return cancellation != null && cancellation.isCancelled();
    }
}
