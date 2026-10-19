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

    private static final String ACCEPTED_BODY = "{\n"
            + "  \"event_type\": \"inbound_email\",\n"
            + "  \"task_id\": \"task-1\",\n"
            + "  \"tenant_id\": \"tenant-1\",\n"
            + "  \"listener_id\": \"listener-1\",\n"
            + "  \"attempt\": 1,\n"
            + "  \"accepted_at_utc\": 1700000000,\n"
            + "  \"occurred_at_utc\": 1700000001,\n"
            + "  \"routing_decision\": {\n"
            + "    \"terminal_action\": \"TERMINAL_ACTION_TYPE_DELIVER_WEBHOOK\",\n"
            + "    \"used_listener_default\": false\n"
            + "  },\n"
            + "  \"envelope\": {\"mail_from\": \"sender@example.com\", \"rcpt_to\": [\"rcpt@example.com\"]},\n"
            + "  \"message\": {\"subject\": \"hi\", \"message_id\": \"<1@example.com>\"},\n"
            + "  \"headers\": [{\"name\": \"Subject\", \"value\": \"hi\"}],\n"
            + "  \"raw_email\": {\n"
            + "    \"url\": \"https://raw.example/messages/task-1.eml\",\n"
            + "    \"token_type\": \"Bearer\",\n"
            + "    \"access_token\": \"secret-token\",\n"
            + "    \"expires_at_utc\": 1700100000,\n"
            + "    \"size_bytes\": 12,\n"
            + "    \"content_type\": \"text/plain\"\n"
            + "  }\n"
            + "}";

    private static final String SMTP_ATTEMPT_BODY = "{\n"
            + "  \"channel\": \"smtp\",\n"
            + "  \"task_id\": \"task-1\",\n"
            + "  \"tenant_id\": \"tenant-1\",\n"
            + "  \"listener_id\": \"listener-1\",\n"
            + "  \"correlation_id\": \"accepted-1\",\n"
            + "  \"status\": \"deferred\",\n"
            + "  \"attempt\": 3,\n"
            + "  \"accepted_at_utc\": 1700000000,\n"
            + "  \"occurred_at_utc\": 1700000001,\n"
            + "  \"source_ip\": \"192.0.2.1\",\n"
            + "  \"destination_domain\": \"example.com\",\n"
            + "  \"remote_host\": \"mx.example.com\",\n"
            + "  \"smtp_code\": 451,\n"
            + "  \"enhanced_status_code\": \"4.7.1\",\n"
            + "  \"reason\": \"smtp_transient\",\n"
            + "  \"remote_response\": \"try later\",\n"
            + "  \"next_retry_at_utc\": 1700000060,\n"
            + "  \"correlation_task_id\": \"corr-1\"\n"
            + "}";

    private static final String S3_ATTEMPT_BODY = "{\n"
            + "  \"event_type\": \"s3_egress_status\",\n"
            + "  \"channel\": \"s3\",\n"
            + "  \"task_id\": \"task-1\",\n"
            + "  \"tenant_id\": \"tenant-1\",\n"
            + "  \"listener_id\": \"listener-1\",\n"
            + "  \"correlation_id\": \"accepted-1\",\n"
            + "  \"status\": \"delivered\",\n"
            + "  \"attempt\": 2,\n"
            + "  \"accepted_at_utc\": 1700000000,\n"
            + "  \"occurred_at_utc\": 1700000001,\n"
            + "  \"storage_ref\": \"storage-1\",\n"
            + "  \"bucket_name\": \"bucket\",\n"
            + "  \"object_key\": \"inbound/a.eml\",\n"
            + "  \"endpoint_host\": \"s3.example.com\",\n"
            + "  \"status_code\": 200\n"
            + "}";

    private static final String WEBHOOK_ATTEMPT_BODY = "{\n"
            + "  \"event_type\": \"webhook_egress_status\",\n"
            + "  \"channel\": \"webhook\",\n"
            + "  \"task_id\": \"task-1\",\n"
            + "  \"tenant_id\": \"tenant-1\",\n"
            + "  \"listener_id\": \"listener-1\",\n"
            + "  \"correlation_id\": \"accepted-1\",\n"
            + "  \"status\": \"invalid\",\n"
            + "  \"attempt\": 1,\n"
            + "  \"status_code\": 500,\n"
            + "  \"message\": \"boom\",\n"
            + "  \"retry_after_ms\": 1000,\n"
            + "  \"occurred_at_utc\": 1700000001,\n"
            + "  \"target_url_host\": \"hooks.example.com\",\n"
            + "  \"webhook_ref\": \"hook-1\",\n"
            + "  \"signing_key_kid\": \"kid-1\",\n"
            + "  \"raw_token_ttl_seconds\": 90000\n"
            + "}";

    @Test
    void decodesAnAcceptedEventAsTheWebhookPayload() {
        NotificationEvent event = NotifyDecoder.decode(ACCEPTED_SUBJECT, bytes(ACCEPTED_BODY));

        AcceptedNotification accepted = assertInstanceOf(AcceptedNotification.class, event);
        assertEquals(NotificationKind.ACCEPTED, accepted.kind());
        assertEquals("task-1", accepted.email().taskId());
        assertEquals("sender@example.com", accepted.email().envelope().mailFrom());
        assertEquals("secret-token", accepted.email().rawEmail().accessToken());
        assertEquals(1, accepted.email().headers().size());
    }

    @Test
    void decodesSmtpS3AndWebhookAttempts() {
        AttemptEvent smtp = assertInstanceOf(AttemptNotification.class,
                NotifyDecoder.decode(ATTEMPT_SUBJECT, bytes(SMTP_ATTEMPT_BODY))).attempt();
        assertEquals(AttemptChannel.SMTP, smtp.channel());
        assertEquals(StatusOutcome.DEFERRED, smtp.status());
        assertEquals("accepted-1", smtp.correlationId());

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
        assertEquals(StatusOutcome.DEFERRED, attempt.status());
        assertEquals(3, attempt.attempt());
        assertEquals(1700000000L, attempt.acceptedAtUtc().longValue());
        assertEquals(1700000001L, attempt.occurredAtUtc());
        assertEquals("192.0.2.1", attempt.sourceIp());
        assertEquals("example.com", attempt.destinationDomain());
        assertEquals("mx.example.com", attempt.remoteHost());
        assertEquals(451, attempt.smtpCode().intValue());
        assertEquals("4.7.1", attempt.enhancedStatusCode());
        assertEquals("smtp_transient", attempt.reason());
        assertEquals("try later", attempt.remoteResponse());
        assertEquals(1700000060L, attempt.nextRetryAtUtc().longValue());
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
        assertEquals(500, attempt.statusCode().intValue());
        assertEquals("boom", attempt.message());
        assertEquals(1000L, attempt.retryAfterMs().longValue());
        assertEquals("hooks.example.com", attempt.targetUrlHost());
        assertEquals("hook-1", attempt.webhookRef());
        assertEquals("kid-1", attempt.signingKeyKid());
        assertEquals(90000L, attempt.rawTokenTtlSeconds().longValue());
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

        String mistyped = "{\n"
                + "  \"channel\": \"smtp\",\n"
                + "  \"task_id\": \"task-1\",\n"
                + "  \"tenant_id\": \"tenant-1\",\n"
                + "  \"listener_id\": \"listener-1\",\n"
                + "  \"status\": \"delivered\",\n"
                + "  \"attempt\": \"one\",\n"
                + "  \"occurred_at_utc\": 1700000001\n"
                + "}";
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
