package com.mxraven.mail.notify;

import com.mxraven.mail.Cancellation;
import com.mxraven.mail.webhook.InboundEmail;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotificationClientTest {
    private static final String ACCEPTED_SUBJECT = "tenant.tenant-1.listener.listener-1.events.accepted";
    private static final String ATTEMPT_SUBJECT = "tenant.tenant-1.listener.listener-1.events.attempt";

    private static final String ACCEPTED_BODY = """
            {
              "event_type": "inbound_email",
              "task_id": "task-1",
              "tenant_id": "tenant-1",
              "listener_id": "listener-1",
              "attempt": 1,
              "accepted_at_utc": 1700000000,
              "occurred_at_utc": 1700000001,
              "envelope": {"mail_from": "sender@example.com", "rcpt_to": ["rcpt@example.com"]},
              "message": {"subject": "hi"},
              "headers": [{"name": "Subject", "value": "hi"}],
              "raw_email": {
                "url": "https://raw.example/messages/task-1.eml",
                "access_token": "secret-token"
              }
            }""";

    private static final String ATTEMPT_BODY = """
            {
              "channel": "smtp",
              "task_id": "task-1",
              "tenant_id": "tenant-1",
              "listener_id": "listener-1",
              "status": "deferred",
              "attempt": 3,
              "occurred_at_utc": 1700000001,
              "reason": "smtp_transient"
            }""";

    private final FakeNotifyTransport.Factory factory = new FakeNotifyTransport.Factory();

    @Test
    void authenticatesWithTheListenerApiKeyAndDerivesTheConsumer() {
        NotificationClient client = connect();

        assertEquals("mxr_tx_ab12cd34ef56", factory.settings.username());
        assertEquals("api-key-secret", factory.settings.secret());
        assertEquals("listener-1", factory.settings.listener());
        assertEquals("notify-listener-1", factory.settings.consumer());
        client.close();
    }

    @Test
    void rejectsWhenTheListenerConsumerDoesNotExist() {
        factory.consumerNotFound = true;

        assertThrows(NotificationException.class, this::connect);
        assertNull(factory.transport);
    }

    @Test
    void defaultsToTheProductionNotificationUrl() {
        NotificationClient client = NotificationClient.connect(
                NotificationClientOptions.builder()
                        .username("mxr_tx_ab12cd34ef56")
                        .secret("api-key-secret")
                        .listener("listener-1")
                        .build(), factory);

        assertEquals(NotificationClient.DEFAULT_URL, factory.settings.url());
        assertEquals("wss://nats.mxraven.com", factory.settings.url());
        client.close();
    }

    @Test
    void rejectsInvalidOptionsBeforeConnecting() {
        List<NotificationClientOptions> invalid = List.of(
                baseOptions().url("https://nats.example").build(),
                baseOptions().username("").build(),
                baseOptions().secret("").build(),
                baseOptions().listener("").build(),
                baseOptions().listener("listener.*").build(),
                baseOptions().listener("listener/1").build(),
                baseOptions().consumer("notify.listener").build(),
                baseOptions().connectTimeout(Duration.ZERO).build());

        for (NotificationClientOptions options : invalid) {
            assertThrows(NotificationException.class,
                    () -> NotificationClient.connect(options, factory));
        }
        assertNull(factory.settings);
    }

    @Test
    void decodesAndAcknowledgesAnAcceptedEvent() throws InterruptedException {
        NotificationClient client = connect();
        factory.transport.enqueue(ACCEPTED_SUBJECT, ACCEPTED_BODY);
        List<NotificationKind> seen = new CopyOnWriteArrayList<>();
        AtomicReference<Throwable> failure = new AtomicReference<>();
        Cancellation cancellation = Cancellation.create();

        Thread thread = consumeInBackground(client, event -> {
            seen.add(event.kind());
            return HandlerResult.CONTINUE;
        }, cancellation, failure);
        awaitSettlements(factory.transport, 1);
        cancellation.cancel();
        join(thread);

        assertNull(failure.get());
        assertEquals(List.of(NotificationKind.ACCEPTED), seen);
        assertEquals(List.of("ack"), factory.transport.settlements());
        client.close();
    }

    @Test
    void redeliversAFailedHandlerWithExponentialBackoff() throws InterruptedException {
        NotificationClient client = connect();
        factory.transport.enqueue(ATTEMPT_SUBJECT, ATTEMPT_BODY, 3);
        AtomicReference<Throwable> failure = new AtomicReference<>();
        Cancellation cancellation = Cancellation.create();

        Thread thread = consumeInBackground(client, event -> {
            throw new IllegalStateException("transient failure");
        }, cancellation, failure);
        awaitSettlements(factory.transport, 1);
        cancellation.cancel();
        join(thread);

        assertNull(failure.get());
        assertEquals(List.of("nak:4000"), factory.transport.settlements());
        client.close();
    }

    @Test
    void computesBackoffDelays() {
        assertEquals(1000L, NotificationClient.retryDelay(1).toMillis());
        assertEquals(2000L, NotificationClient.retryDelay(2).toMillis());
        assertEquals(4000L, NotificationClient.retryDelay(3).toMillis());
        assertEquals(30000L, NotificationClient.retryDelay(10).toMillis());
        assertEquals(30000L, NotificationClient.retryDelay(50).toMillis());
    }

    @Test
    void acknowledgesAndStopsWhenTheHandlerReturnsStop() {
        NotificationClient client = connect();
        factory.transport.enqueue(ACCEPTED_SUBJECT, ACCEPTED_BODY);
        List<NotificationKind> seen = new ArrayList<>();

        client.consume(event -> {
            seen.add(event.kind());
            return HandlerResult.STOP;
        });

        assertEquals(List.of(NotificationKind.ACCEPTED), seen);
        assertEquals(List.of("ack"), factory.transport.settlements());
        client.close();
    }

    @Test
    void dispatchesTypedCallbacks() {
        NotificationClient client = connect();
        factory.transport.enqueue(ACCEPTED_SUBJECT, ACCEPTED_BODY);
        factory.transport.enqueue(ATTEMPT_SUBJECT, ATTEMPT_BODY);
        List<String> seen = new ArrayList<>();

        client.consume(new NotificationCallbacks() {
            @Override
            public HandlerResult onAccepted(InboundEmail email) {
                seen.add("accepted " + email.taskId());
                return HandlerResult.CONTINUE;
            }

            @Override
            public HandlerResult onAttempt(AttemptEvent event) {
                seen.add("attempt " + event.channel());
                return HandlerResult.STOP;
            }
        });

        assertEquals(List.of("accepted task-1", "attempt SMTP"), seen);
        assertEquals(List.of("ack", "ack"), factory.transport.settlements());
        client.close();
    }

    @Test
    void terminatesAnUndecodableEventAndThrows() {
        NotificationClient client = connect();
        factory.transport.enqueue(ACCEPTED_SUBJECT, "{");

        NotificationDecodeException thrown = assertThrows(NotificationDecodeException.class,
                () -> client.consume(event -> HandlerResult.CONTINUE));

        assertEquals(ACCEPTED_SUBJECT, thrown.subject());
        assertEquals(List.of("term"), factory.transport.settlements());
        client.close();
    }

    @Test
    void reportsAnUndecodableEventAndContinuesWhenAnErrorHandlerIsSet() throws InterruptedException {
        List<NotificationException> errors = new CopyOnWriteArrayList<>();
        NotificationClient client = NotificationClient.connect(
                baseOptions().onError(errors::add).build(), factory);
        factory.transport.enqueue(ACCEPTED_SUBJECT, "{");
        factory.transport.enqueue(ATTEMPT_SUBJECT, ATTEMPT_BODY);
        List<NotificationKind> handled = new CopyOnWriteArrayList<>();
        AtomicReference<Throwable> failure = new AtomicReference<>();
        Cancellation cancellation = Cancellation.create();

        Thread thread = consumeInBackground(client, event -> {
            handled.add(event.kind());
            return HandlerResult.CONTINUE;
        }, cancellation, failure);
        awaitSettlements(factory.transport, 2);
        cancellation.cancel();
        join(thread);

        assertNull(failure.get());
        assertEquals(1, errors.size());
        assertInstanceOf(NotificationDecodeException.class, errors.get(0));
        assertEquals(List.of(NotificationKind.ATTEMPT), handled);
        assertEquals(List.of("term", "ack"), factory.transport.settlements());
        client.close();
    }

    @Test
    void stopsCleanlyWhenCancelled() throws InterruptedException {
        NotificationClient client = connect();
        AtomicReference<Throwable> failure = new AtomicReference<>();
        Cancellation cancellation = Cancellation.create();

        Thread thread = consumeInBackground(client, event -> HandlerResult.CONTINUE,
                cancellation, failure);
        Thread.sleep(100);
        cancellation.cancel();
        join(thread);

        assertNull(failure.get());
        client.close();
    }

    @Test
    void stopsConsumeWhenTheClientCloses() throws InterruptedException {
        NotificationClient client = connect();
        AtomicReference<Throwable> failure = new AtomicReference<>();

        Thread thread = consumeInBackground(client, event -> HandlerResult.CONTINUE,
                null, failure);
        Thread.sleep(100);
        client.close();
        join(thread);

        assertNull(failure.get());
    }

    @Test
    void rejectsConsumeAfterTheClientIsClosed() {
        NotificationClient client = connect();
        client.close();

        assertThrows(NotificationException.class,
                () -> client.consume(event -> HandlerResult.CONTINUE));
    }

    @Test
    void eventsYieldDeliveriesForManualSettlement() {
        NotificationClient client = connect();
        factory.transport.enqueue(ACCEPTED_SUBJECT, ACCEPTED_BODY);
        NotificationIterator iterator = client.events();

        NotificationDelivery delivery = iterator.next();
        assertInstanceOf(AcceptedNotification.class, delivery.event());
        delivery.ack();

        assertEquals(List.of("ack"), factory.transport.settlements());
        iterator.close();
        client.close();
    }

    @Test
    void eventsSupportDelayedNakAndTermination() {
        NotificationClient client = connect();
        factory.transport.enqueue(ACCEPTED_SUBJECT, ACCEPTED_BODY);
        factory.transport.enqueue(ATTEMPT_SUBJECT, ATTEMPT_BODY);
        NotificationIterator iterator = client.events();

        iterator.next().nak(Duration.ofSeconds(5));
        iterator.next().term();

        assertEquals(List.of("nak:5000", "term"), factory.transport.settlements());
        iterator.close();
        client.close();
    }

    @Test
    void eventsRejectAnUndecodableEvent() {
        NotificationClient client = connect();
        factory.transport.enqueue(ACCEPTED_SUBJECT, "{");
        NotificationIterator iterator = client.events();

        assertThrows(NotificationDecodeException.class, iterator::next);
        assertEquals(List.of("term"), factory.transport.settlements());
        iterator.close();
        client.close();
    }

    @Test
    void eventsStopWhenTheClientCloses() throws InterruptedException {
        NotificationClient client = connect();
        NotificationIterator iterator = client.events();
        AtomicBoolean result = new AtomicBoolean(true);

        Thread thread = new Thread(() -> result.set(iterator.hasNext()));
        thread.start();
        client.close();
        join(thread);

        assertFalse(result.get());
    }

    @Test
    void eventsRejectUseAfterTheClientIsClosed() {
        NotificationClient client = connect();
        client.close();
        NotificationIterator iterator = client.events();

        assertThrows(NotificationException.class, iterator::next);
    }

    @Test
    void eventsCloseIsIdempotent() {
        NotificationClient client = connect();
        NotificationIterator iterator = client.events();

        iterator.close();
        iterator.close();

        client.close();
    }

    @Test
    void deliveryCannotBeSettledTwice() {
        NotificationClient client = connect();
        factory.transport.enqueue(ACCEPTED_SUBJECT, ACCEPTED_BODY);
        NotificationIterator iterator = client.events();

        NotificationDelivery delivery = iterator.next();
        delivery.ack();
        assertThrows(NotificationException.class, delivery::ack);

        iterator.close();
        client.close();
    }

    @Test
    void closeIsIdempotent() {
        NotificationClient client = connect();

        client.close();
        client.close();

        assertTrue(client.isClosed());
    }

    private NotificationClientOptions.Builder baseOptions() {
        return NotificationClientOptions.builder()
                .url("wss://nats.example")
                .username("mxr_tx_ab12cd34ef56")
                .secret("api-key-secret")
                .listener("listener-1")
                .connectTimeout(Duration.ofSeconds(2));
    }

    private NotificationClient connect() {
        return NotificationClient.connect(baseOptions().build(), factory);
    }

    private static Thread consumeInBackground(NotificationClient client, NotificationHandler handler,
                                              Cancellation cancellation, AtomicReference<Throwable> failure) {
        Thread thread = new Thread(() -> {
            try {
                client.consume(handler, cancellation);
            } catch (Throwable thrown) {
                failure.set(thrown);
            }
        });
        thread.start();
        return thread;
    }

    private static void awaitSettlements(FakeNotifyTransport transport, int count)
            throws InterruptedException {
        long deadline = System.nanoTime() + Duration.ofSeconds(5).toNanos();
        while (System.nanoTime() < deadline && transport.settlements().size() < count) {
            Thread.sleep(10);
        }
        assertTrue(transport.settlements().size() >= count,
                "expected at least " + count + " settlement(s), got " + transport.settlements());
    }

    private static void join(Thread thread) throws InterruptedException {
        thread.join(5_000);
        assertFalse(thread.isAlive(), "consume did not stop");
    }
}
