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
        String body = "{\n  \"task_id\": \"task-1\",\n  \"status\": \"deferred\",\n  \"attempt\": 2,\n  \"smtp_code\": 451,\n  \"enhanced_status_code\": \"4.7.1\",\n  \"remote_response\": \"greylisted\",\n  \"next_retry_at_utc\": 1789303000\n}";

        WebhookEvent event = WebhookEvent.decode(bytes(body));

        DeliveryStatus status = assertInstanceOf(DeliveryStatus.class, event);
        assertEquals(WebhookEventType.DELIVERY_STATUS, status.type());
        assertEquals(StatusOutcome.DEFERRED, status.status());
        assertEquals(451, status.smtpCode());
        assertEquals(1789303000L, status.nextRetryAtUtc());
    }

    @Test
    void decodesAStorageStatus() {
        String body = "{\n  \"event_type\": \"s3_egress_status\",\n  \"task_id\": \"task-1\",\n  \"status\": \"delivered\",\n  \"attempt\": 1,\n  \"object_key\": \"2026/09/task-1.eml\"\n}";

        WebhookEvent event = WebhookEvent.decode(bytes(body));

        StorageStatus status = assertInstanceOf(StorageStatus.class, event);
        assertEquals(WebhookEventType.STORAGE_STATUS, status.type());
        assertEquals(StatusOutcome.DELIVERED, status.status());
        assertEquals("2026/09/task-1.eml", status.objectKey());
    }

    @Test
    void rejectsUnrecognizedPayloads() {
        assertThrows(WebhookException.class, () -> WebhookEvent.decode(bytes("{")));
        assertThrows(WebhookException.class, () -> WebhookEvent.decode(bytes("")));
        assertThrows(WebhookException.class, () -> WebhookEvent.decode(bytes("{\"event_type\":\"something_else\"}")));
        assertThrows(WebhookException.class, () -> WebhookEvent.decode(null));
    }
}
