package com.mxraven.mail.webhook;

import com.mxraven.mail.internal.Java8;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.mxraven.mail.webhook.WebhookTestSupport.INBOUND_BODY;
import static com.mxraven.mail.webhook.WebhookTestSupport.SECRET;
import static com.mxraven.mail.webhook.WebhookTestSupport.URI;
import static com.mxraven.mail.webhook.WebhookTestSupport.bytes;
import static com.mxraven.mail.webhook.WebhookTestSupport.signedHeaders;

class WebhookHandlerTest {

    private static String now() {
        return String.valueOf(Instant.now().getEpochSecond());
    }

    private static WebhookHandler handler(WebhookListener listener) {
        return WebhookHandler.builder()
                .verifier(WebhookVerifier.builder().secret(SECRET).build())
                .listener(listener)
                .build();
    }

    @Test
    void acceptsAndDispatchesAValidRequest() {
        AtomicReference<WebhookEvent> received = new AtomicReference<>();
        byte[] body = bytes(INBOUND_BODY);
        Map<String, String> headers = signedHeaders("POST", URI, body, now(), "task-123", SECRET);

        WebhookResult result = handler(received::set).handle("POST", URI, headers, body);

        assertEquals(WebhookResult.ACCEPTED, result);
        assertEquals(204, result.status());
        assertInstanceOf(InboundEmail.class, received.get());
    }

    @Test
    void reportsAnInvalidSignatureWithoutDispatching() {
        AtomicReference<WebhookEvent> received = new AtomicReference<>();
        byte[] body = bytes(INBOUND_BODY);
        Map<String, String> headers = signedHeaders("POST", URI, body, now(), "task-123", SECRET);

        WebhookResult result = handler(received::set).handle("POST", URI, headers, bytes(INBOUND_BODY + " "));

        assertEquals(WebhookResult.INVALID_SIGNATURE, result);
        assertEquals(401, result.status());
        assertNull(received.get());
    }

    @Test
    void reportsMissingHeadersAsBadRequest() {
        byte[] body = bytes(INBOUND_BODY);

        assertEquals(WebhookResult.BAD_REQUEST,
                handler(event -> { }).handle("POST", URI, Java8.map(), body));
    }

    @Test
    void deduplicatesRepeatedTasks() {
        byte[] body = bytes(INBOUND_BODY);
        Map<String, String> headers = signedHeaders("POST", URI, body, now(), "task-123", SECRET);
        Set<String> remembered = ConcurrentHashMap.newKeySet();
        WebhookStore store = new WebhookStore() {
            @Override
            public boolean seen(String taskId) {
                return remembered.contains(taskId);
            }

            @Override
            public void remember(String taskId) {
                remembered.add(taskId);
            }
        };
        AtomicInteger dispatched = new AtomicInteger();
        WebhookHandler handler = WebhookHandler.builder()
                .verifier(WebhookVerifier.builder().secret(SECRET).build())
                .listener(event -> dispatched.incrementAndGet())
                .store(store)
                .build();

        assertEquals(WebhookResult.ACCEPTED, handler.handle("POST", URI, headers, body));
        assertTrue(remembered.contains("task-123"));
        assertEquals(WebhookResult.DUPLICATE, handler.handle("POST", URI, headers, body));
        assertEquals(WebhookResult.DUPLICATE, handler.handle("POST", URI, headers, body));
        assertEquals(1, dispatched.get());
    }

    @Test
    void propagatesListenerFailures() {
        byte[] body = bytes(INBOUND_BODY);
        Map<String, String> headers = signedHeaders("POST", URI, body, now(), "task-123", SECRET);
        WebhookHandler handler = handler(event -> {
            throw new IllegalStateException("boom");
        });

        assertThrows(IllegalStateException.class, () -> handler.handle("POST", URI, headers, body));
    }

    @Test
    void requiresAVerifierAndListener() {
        assertThrows(IllegalStateException.class, () -> WebhookHandler.builder().build());
        assertThrows(IllegalArgumentException.class, () -> WebhookHandler.builder().verifier(null));
        assertThrows(IllegalArgumentException.class, () -> WebhookHandler.builder().listener(null));
    }
}
