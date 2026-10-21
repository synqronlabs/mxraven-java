package com.mxraven.mail.notify;

import com.mxraven.mail.webhook.StatusOutcome;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NotificationDecodeTest {
    private static final String ACCEPTED_SUBJECT = "tenant.tenant-1.listener.listener-1.events.accepted";
    private static final String ATTEMPT_SUBJECT = "tenant.tenant-1.listener.listener-1.events.attempt";

    private static final String ACCEPTED_BODY = """
            {
              "event_type": "inbound_email",
              "task_id": "task-1",
              "tenant_id": "tenant-1",
              "listener_id": "listener-1",
              "message_ref": "message-ref-1",
              "attempt": 1,
              "accepted_at_utc": 1700000000,
              "occurred_at_utc": 1700000001,
              "routing_decision": {
                "terminal_action": "TERMINAL_ACTION_TYPE_DELIVER_WEBHOOK",
                "used_listener_default": false
              },
              "envelope": {"mail_from": "sender@example.com", "rcpt_to": ["rcpt@example.com"]},
              "message": {"subject": "hi", "message_id": "<1@example.com>"},
              "headers": [{"name": "Subject", "value": "hi"}],
              "raw_email": {
                "url": "https://raw.example/messages/task-1.eml",
                "token_type": "Bearer",
                "access_token": "secret-token",
                "expires_at_utc": 1700100000,
                "size_bytes": 12,
                "content_type": "text/plain"
              }
            }""";

    private static final String SMTP_ATTEMPT_BODY = """
            {
              "channel": "smtp",
              "task_id": "task-1",
              "tenant_id": "tenant-1",
              "listener_id": "listener-1",
              "message_ref": "message-ref-1",
              "correlation_id": "accepted-1",
              "status": "deferred",
              "attempt": 3,
              "accepted_at_utc": 1700000000,
              "occurred_at_utc": 1700000001,
              "source_ip": "192.0.2.1",
              "destination_domain": "example.com",
              "remote_host": "mx.example.com",
              "smtp_code": 451,
              "enhanced_status_code": "4.7.1",
              "reason": "smtp_transient",
              "remote_response": "try later",
              "next_retry_at_utc": 1700000060,
              "correlation_task_id": "corr-1"
            }""";

    private static final String S3_ATTEMPT_BODY = """
            {
              "event_type": "s3_egress_status",
              "channel": "s3",
              "task_id": "task-1",
              "tenant_id": "tenant-1",
              "listener_id": "listener-1",
              "message_ref": "message-ref-1",
              "correlation_id": "accepted-1",
              "status": "delivered",
              "attempt": 2,
              "accepted_at_utc": 1700000000,
              "occurred_at_utc": 1700000001,
              "storage_ref": "storage-1",
              "bucket_name": "bucket",
              "object_key": "inbound/a.eml",
              "endpoint_host": "s3.example.com",
              "status_code": 200
            }""";

    private static final String WEBHOOK_ATTEMPT_BODY = """
            {
              "event_type": "webhook_egress_status",
              "channel": "webhook",
              "task_id": "task-1",
              "tenant_id": "tenant-1",
              "listener_id": "listener-1",
              "message_ref": "message-ref-1",
              "correlation_id": "accepted-1",
              "status": "invalid",
              "attempt": 1,
              "status_code": 500,
              "message": "boom",
              "retry_after_ms": 1000,
              "occurred_at_utc": 1700000001,
              "target_url_host": "hooks.example.com",
              "webhook_ref": "hook-1",
              "signing_key_kid": "kid-1",
              "raw_token_ttl_seconds": 90000
            }""";

    @Test
    void decodesAnAcceptedEventAsTheWebhookPayload() {
        NotificationEvent event = NotifyDecoder.decode(ACCEPTED_SUBJECT, bytes(ACCEPTED_BODY));

        AcceptedNotification notification = assertInstanceOf(AcceptedNotification.class, event);
        assertEquals(NotificationKind.ACCEPTED, notification.kind());
        assertEquals("task-1", notification.email().taskId());
        assertEquals("message-ref-1", notification.email().messageRef());
        assertEquals("sender@example.com", notification.email().envelope().mailFrom());
        assertEquals("secret-token", notification.email().rawEmail().accessToken());
        assertEquals(1, notification.email().headers().size());
    }

    @Test
    void decodesSmtpS3AndWebhookAttempts() {
        AttemptEvent smtp = assertInstanceOf(AttemptNotification.class,
                NotifyDecoder.decode(ATTEMPT_SUBJECT, bytes(SMTP_ATTEMPT_BODY))).attempt();
        assertEquals(AttemptChannel.SMTP, smtp.channel());
        assertEquals(StatusOutcome.DEFERRED, smtp.status());
        assertEquals("accepted-1", smtp.correlationId());
        assertEquals("message-ref-1", smtp.messageRef());

        AttemptEvent s3 = assertInstanceOf(AttemptNotification.class,
                NotifyDecoder.decode(ATTEMPT_SUBJECT, bytes(S3_ATTEMPT_BODY))).attempt();
        assertEquals(AttemptChannel.S3, s3.channel());
        assertEquals(StatusOutcome.DELIVERED, s3.status());
        assertEquals("bucket", s3.bucketName());

        AttemptEvent webhook = assertInstanceOf(AttemptNotification.class,
                NotifyDecoder.decode(ATTEMPT_SUBJECT, bytes(WEBHOOK_ATTEMPT_BODY))).attempt();
        assertEquals(AttemptChannel.WEBHOOK, webhook.channel());
        assertEquals(StatusOutcome.INVALID, webhook.status());
        assertEquals("hook-1", webhook.webhookRef());
    }

    @Test
    void mapsEverySmtpAttemptField() {
        AttemptEvent attempt = assertInstanceOf(AttemptNotification.class,
                NotifyDecoder.decode("x.events.attempt", bytes(SMTP_ATTEMPT_BODY))).attempt();

        assertNull(attempt.eventType());
        assertEquals(AttemptChannel.SMTP, attempt.channel());
        assertEquals("task-1", attempt.taskId());
        assertEquals("tenant-1", attempt.tenantId());
        assertEquals("listener-1", attempt.listenerId());
        assertEquals("accepted-1", attempt.correlationId());
        assertEquals("message-ref-1", attempt.messageRef());
        assertEquals(StatusOutcome.DEFERRED, attempt.status());
        assertEquals(3, attempt.attempt());
        assertEquals(1700000000L, attempt.acceptedAtUtc());
        assertEquals(1700000001L, attempt.occurredAtUtc());
        assertEquals("192.0.2.1", attempt.sourceIp());
        assertEquals("example.com", attempt.destinationDomain());
        assertEquals("mx.example.com", attempt.remoteHost());
        assertEquals(451, attempt.smtpCode());
        assertEquals("4.7.1", attempt.enhancedStatusCode());
        assertEquals("smtp_transient", attempt.reason());
        assertEquals("try later", attempt.remoteResponse());
        assertEquals(1700000060L, attempt.nextRetryAtUtc());
        assertEquals("corr-1", attempt.correlationTaskId());
        assertNull(attempt.storageRef());
    }

    @Test
    void mapsEveryWebhookAttemptField() {
        AttemptEvent attempt = assertInstanceOf(AttemptNotification.class,
                NotifyDecoder.decode("x.events.attempt", bytes(WEBHOOK_ATTEMPT_BODY))).attempt();

        assertEquals("webhook_egress_status", attempt.eventType());
        assertEquals(AttemptChannel.WEBHOOK, attempt.channel());
        assertEquals(StatusOutcome.INVALID, attempt.status());
        assertEquals(500, attempt.statusCode());
        assertEquals("boom", attempt.message());
        assertEquals(1000L, attempt.retryAfterMs());
        assertEquals("hooks.example.com", attempt.targetUrlHost());
        assertEquals("hook-1", attempt.webhookRef());
        assertEquals("kid-1", attempt.signingKeyKid());
        assertEquals(90000L, attempt.rawTokenTtlSeconds());
    }

    @Test
    void fallsBackToTheBodyShapeWhenTheSubjectIsUnknown() {
        assertInstanceOf(AcceptedNotification.class,
                NotifyDecoder.decode("", bytes(ACCEPTED_BODY)));
        assertInstanceOf(AcceptedNotification.class,
                NotifyDecoder.decode("unknown", bytes(ACCEPTED_BODY)));
        assertInstanceOf(AttemptNotification.class,
                NotifyDecoder.decode("", bytes(S3_ATTEMPT_BODY)));
        assertInstanceOf(AttemptNotification.class,
                NotifyDecoder.decode("unknown", bytes(S3_ATTEMPT_BODY)));
    }

    @Test
    void reportsInvalidJsonWithTheSubject() {
        NotificationDecodeException thrown = assertThrows(NotificationDecodeException.class,
                () -> NotifyDecoder.decode("x.events.accepted", bytes("{")));

        assertEquals("x.events.accepted", thrown.subject());
    }

    @Test
    void reportsUnrecognizedAndMistypedBodies() {
        assertThrows(NotificationDecodeException.class,
                () -> NotifyDecoder.decode("x.events.unknown", bytes("{\"foo\":\"bar\"}")));
        assertThrows(NotificationDecodeException.class,
                () -> NotifyDecoder.decode("x.events.unknown", bytes("[1,2]")));

        String mistyped = """
                {
                  "channel": "smtp",
                  "task_id": "task-1",
                  "tenant_id": "tenant-1",
                  "listener_id": "listener-1",
                  "status": "delivered",
                  "attempt": "one",
                  "occurred_at_utc": 1700000001
                }""";
        assertThrows(NotificationDecodeException.class,
                () -> NotifyDecoder.decode("x.events.attempt", bytes(mistyped)));
    }

    @Test
    void rejectsUnknownChannelAndStatusValues() {
        String unknownChannel = SMTP_ATTEMPT_BODY.replace("\"smtp\"", "\"carrier_pigeon\"");
        assertThrows(NotificationDecodeException.class,
                () -> NotifyDecoder.decode("x.events.attempt", bytes(unknownChannel)));

        String unknownStatus = SMTP_ATTEMPT_BODY.replace("\"deferred\"", "\"exploded\"");
        assertThrows(NotificationDecodeException.class,
                () -> NotifyDecoder.decode("x.events.attempt", bytes(unknownStatus)));
    }

    private static byte[] bytes(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }
}
