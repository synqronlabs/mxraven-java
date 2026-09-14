package com.mxraven.mail.webhook;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.mxraven.mail.webhook.WebhookTestSupport.INBOUND_BODY;
import static com.mxraven.mail.webhook.WebhookTestSupport.bytes;

class WebhookCallbacksTest {

    private static final String DELIVERY_BODY =
            "{\"task_id\":\"task-1\",\"status\":\"delivered\",\"attempt\":1}";
    private static final String STORAGE_BODY =
            "{\"event_type\":\"s3_egress_status\",\"task_id\":\"task-1\",\"status\":\"delivered\",\"attempt\":1}";

    @Test
    void dispatchesOnlyToOverriddenCallbacks() {
        AtomicInteger inbound = new AtomicInteger();
        WebhookCallbacks callbacks = new WebhookCallbacks() {
            @Override
            public void onInboundEmail(InboundEmail email) {
                inbound.incrementAndGet();
            }
        };

        callbacks.onEvent(WebhookEvent.decode(bytes(INBOUND_BODY)));
        callbacks.onEvent(WebhookEvent.decode(bytes(DELIVERY_BODY)));
        callbacks.onEvent(WebhookEvent.decode(bytes(STORAGE_BODY)));

        assertEquals(1, inbound.get());
    }

    @Test
    void reachesEveryTypedCallback() {
        AtomicInteger calls = new AtomicInteger();
        WebhookCallbacks callbacks = new WebhookCallbacks() {
            @Override
            public void onInboundEmail(InboundEmail email) {
                calls.incrementAndGet();
            }

            @Override
            public void onDeliveryStatus(DeliveryStatus status) {
                calls.incrementAndGet();
            }

            @Override
            public void onStorageStatus(StorageStatus storage) {
                calls.incrementAndGet();
            }
        };

        callbacks.onEvent(WebhookEvent.decode(bytes(INBOUND_BODY)));
        callbacks.onEvent(WebhookEvent.decode(bytes(DELIVERY_BODY)));
        callbacks.onEvent(WebhookEvent.decode(bytes(STORAGE_BODY)));

        assertEquals(3, calls.get());
    }

    @Test
    void isUsableAnywhereAListenerIsAccepted() {
        AtomicInteger calls = new AtomicInteger();
        WebhookListener listener = new WebhookCallbacks() {
            @Override
            public void onDeliveryStatus(DeliveryStatus status) {
                calls.incrementAndGet();
            }
        };

        listener.onEvent(WebhookEvent.decode(bytes(DELIVERY_BODY)));

        assertEquals(1, calls.get());
    }
}
