package com.mxraven.mail.webhook;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static com.mxraven.mail.webhook.WebhookTestSupport.INBOUND_BODY;
import static com.mxraven.mail.webhook.WebhookTestSupport.bytes;

class WebhookDecodeTest {

    @Test
    void decodesAnInboundEmail() {
        WebhookEvent event = WebhookEvent.decode(bytes(INBOUND_BODY));

        InboundEmail email = assertInstanceOf(InboundEmail.class, event);
        assertEquals(WebhookEventType.INBOUND_EMAIL, email.type());
        assertEquals("sender@example.net", email.envelope().mailFrom());
        assertEquals(1, email.headers().size());
        assertEquals("From", email.headers().get(0).name());
        assertEquals("token", email.rawEmail().accessToken());
        assertNull(email.verdicts());
    }

    @Test
    void decodesAnSmtpDeliveryStatus() {
        String body = """
                {
                  "task_id": "task-1",
                  "status": "deferred",
                  "attempt": 2,
                  "smtp_code": 451,
                  "enhanced_status_code": "4.7.1",
                  "remote_response": "greylisted",
                  "next_retry_at_utc": 1789303000
                }""";

        WebhookEvent event = WebhookEvent.decode(bytes(body));

        DeliveryStatus status = assertInstanceOf(DeliveryStatus.class, event);
        assertEquals(WebhookEventType.DELIVERY_STATUS, status.type());
        assertEquals(StatusOutcome.DEFERRED, status.status());
        assertEquals(451, status.smtpCode());
        assertEquals(1789303000L, status.nextRetryAtUtc());
    }

    @Test
    void decodesAStorageStatus() {
        String body = """
                {
                  "event_type": "s3_egress_status",
                  "task_id": "task-1",
                  "status": "delivered",
                  "attempt": 1,
                  "object_key": "2026/09/task-1.eml"
                }""";

        WebhookEvent event = WebhookEvent.decode(bytes(body));

        StorageStatus status = assertInstanceOf(StorageStatus.class, event);
        assertEquals(WebhookEventType.STORAGE_STATUS, status.type());
        assertEquals(StatusOutcome.DELIVERED, status.status());
        assertEquals("2026/09/task-1.eml", status.objectKey());
    }

    @Test
    void decodesTheSmtpReasonClassification() {
        String body = """
                {
                  "task_id": "task-1",
                  "tenant_id": "tenant-1",
                  "listener_id": "listener-1",
                  "status": "deferred",
                  "attempt": 2,
                  "accepted_at_utc": 1700000000,
                  "occurred_at_utc": 1700000001,
                  "reason": "smtp_transient"
                }""";

        WebhookEvent event = WebhookEvent.decode(bytes(body));

        DeliveryStatus status = assertInstanceOf(DeliveryStatus.class, event);
        assertEquals("smtp_transient", status.reason());
    }

    @Test
    void decodesAnInvalidWebhookEgressStatus() {
        String body = """
                {
                  "event_type": "webhook_egress_status",
                  "channel": "webhook",
                  "task_id": "task-1",
                  "status": "invalid",
                  "attempt": 1,
                  "occurred_at_utc": 1700000001,
                  "status_code": 0
                }""";

        WebhookEvent event = WebhookEvent.decode(bytes(body));

        DeliveryStatus status = assertInstanceOf(DeliveryStatus.class, event);
        assertEquals(StatusOutcome.INVALID, status.status());
    }

    @Test
    void rejectsUnrecognizedPayloads() {
        assertThrows(WebhookException.class, () -> WebhookEvent.decode(bytes("{")));
        assertThrows(WebhookException.class, () -> WebhookEvent.decode(bytes("")));
        assertThrows(WebhookException.class, () -> WebhookEvent.decode(bytes("{\"event_type\":\"something_else\"}")));
        assertThrows(WebhookException.class, () -> WebhookEvent.decode(null));
    }
}
