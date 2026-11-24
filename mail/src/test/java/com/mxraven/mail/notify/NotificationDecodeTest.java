package com.mxraven.mail.notify;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotificationDecodeTest {
    private static final String ACCEPTED_SUBJECT = "tenant.tenant-1.listener.listener-1.events.accepted";
    private static final String ATTEMPT_SUBJECT = "tenant.tenant-1.listener.listener-1.events.attempt";
    private static final String FAULT_SUBJECT = "tenant.tenant-1.listener.listener-1.events.fault";

    private static final String ACCEPTED_BODY = "{\n"
            + "  \"event_type\": \"inbound_email\",\n"
            + "  \"task_id\": \"task-1\",\n"
            + "  \"tenant_id\": \"tenant-1\",\n"
            + "  \"listener_id\": \"listener-1\",\n"
            + "  \"message_ref\": \"message-ref-1\",\n"
            + "  \"attempt\": 1,\n"
            + "  \"accepted_at_utc\": 1700000000,\n"
            + "  \"occurred_at_utc\": 1700000001,\n"
            + "  \"routing_decision\": {\n"
            + "    \"terminal_action\": \"TERMINAL_ACTION_TYPE_DELIVER\",\n"
            + "    \"used_listener_default\": false,\n"
            + "    \"script_revision_id\": \"rev-1\",\n"
            + "    \"script_revision\": 3,\n"
            + "    \"intents\": [{\"kind\": \"header.set\", \"header\": \"X-Test\", \"value\": \"1\"}],\n"
            + "    \"skipped\": [{\"intent\": {\"kind\": \"forward.smtp\", \"ref\": \"fwd-1\"}, \"reason\": \"cover\"}],\n"
            + "    \"log\": [\"checked\"],\n"
            + "    \"http_calls\": [{\"endpoint\": \"risk\", \"status\": 200, \"duration_ms\": 4,\n"
            + "                    \"request_bytes\": 2, \"response_bytes\": 8}]\n"
            + "  },\n"
            + "  \"verdicts\": {\"action\": \"no action\", \"score\": 1.5, \"required_score\": 5,\n"
            + "               \"is_spam\": false, \"has_malware\": false, \"malware_names\": [],\n"
            + "               \"is_skipped\": false, \"error\": \"\"},\n"
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
            + "  \"message_ref\": \"message-ref-1\",\n"
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
            + "  \"message_ref\": \"message-ref-1\",\n"
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

    private static final String FAULT_BODY = "{\n"
            + "  \"tenant_id\": \"tenant-1\",\n"
            + "  \"listener_id\": \"listener-1\",\n"
            + "  \"message_ref\": \"message-ref-1\",\n"
            + "  \"occurred_at_utc\": 1700000002,\n"
            + "  \"script_revision_id\": \"rev-1\",\n"
            + "  \"script_revision\": 4,\n"
            + "  \"mail_from\": \"sender@example.com\",\n"
            + "  \"recipient\": \"rcpt@example.com\",\n"
            + "  \"smtp_response\": \"451 4.3.0 routing script fault\",\n"
            + "  \"fault\": {\"reason\": \"runtime_error\", \"message\": \"boom\", \"line\": 7},\n"
            + "  \"log\": [\"started\"],\n"
            + "  \"http_calls\": [{\"endpoint\": \"risk\", \"status\": 200, \"duration_ms\": 3,\n"
            + "                  \"request_bytes\": 1, \"response_bytes\": 2, \"cached\": true}],\n"
            + "  \"suppressed_faults\": 5\n"
            + "}";

    @Test
    void decodesAnAcceptedEventWithTheRoutingDecision() {
        NotificationEvent event = NotifyDecoder.decode(ACCEPTED_SUBJECT, bytes(ACCEPTED_BODY));

        AcceptedNotification notification = assertInstanceOf(AcceptedNotification.class, event);
        assertEquals(NotificationKind.ACCEPTED, notification.kind());
        AcceptedMessage message = notification.message();
        assertEquals("task-1", message.taskId());
        assertEquals("message-ref-1", message.messageRef());
        assertEquals("sender@example.com", message.envelope().mailFrom());
        assertEquals("secret-token", message.rawEmail().accessToken());
        assertEquals(1, message.headers().size());

        RoutingDecision decision = message.routingDecision();
        assertEquals(TerminalAction.DELIVER, decision.terminalAction());
        assertEquals(3, decision.scriptRevision());
        assertEquals(RoutingIntentKind.HEADER_SET, decision.intents().get(0).kind());
        assertEquals(RoutingIntentKind.FORWARD_SMTP, decision.skipped().get(0).intent().kind());
        assertEquals("checked", decision.log().get(0));
        assertEquals(200, decision.httpCalls().get(0).status());
        assertEquals(1.5, message.verdicts().score());
    }

    @Test
    void decodesSmtpAndS3Attempts() {
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
    }

    @Test
    void mapsEverySmtpAttemptField() {
        AttemptEvent attempt = assertInstanceOf(AttemptNotification.class,
                NotifyDecoder.decode("x.events.attempt", bytes(SMTP_ATTEMPT_BODY))).attempt();

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
    void decodesSampledFaultEvents() {
        NotificationEvent event = NotifyDecoder.decode(FAULT_SUBJECT, bytes(FAULT_BODY));

        FaultNotification notification = assertInstanceOf(FaultNotification.class, event);
        assertEquals(NotificationKind.FAULT, notification.kind());
        FaultEvent fault = notification.fault();
        assertEquals("message-ref-1", fault.messageRef());
        assertEquals(4, fault.scriptRevision());
        assertEquals("rcpt@example.com", fault.recipient());
        assertEquals("451 4.3.0 routing script fault", fault.smtpResponse());
        assertEquals("runtime_error", fault.fault().reason());
        assertEquals(5, fault.suppressedFaults());
        assertEquals(1, fault.log().size());
        assertTrue(fault.httpCalls().get(0).cached());
    }

    @Test
    void toleratesTheLegacyEventTypeField() {
        assertInstanceOf(AcceptedNotification.class,
                NotifyDecoder.decode(ACCEPTED_SUBJECT, bytes(ACCEPTED_BODY)));
        assertInstanceOf(AttemptNotification.class,
                NotifyDecoder.decode(ATTEMPT_SUBJECT, bytes(S3_ATTEMPT_BODY)));
    }

    @Test
    void fallsBackToTheBodyShapeWhenTheSubjectIsUnknown() {
        assertInstanceOf(AcceptedNotification.class,
                NotifyDecoder.decode("", bytes(ACCEPTED_BODY)));
        assertInstanceOf(AttemptNotification.class,
                NotifyDecoder.decode("", bytes(S3_ATTEMPT_BODY)));
        assertInstanceOf(FaultNotification.class,
                NotifyDecoder.decode("", bytes(FAULT_BODY)));
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

    @Test
    void requiresARoutingDecisionAndEnvelopeOnAcceptedEvents() {
        String noDecision = ACCEPTED_BODY.replace("\"routing_decision\":", "\"decision_removed\":");
        assertThrows(NotificationDecodeException.class,
                () -> NotifyDecoder.decode(ACCEPTED_SUBJECT, bytes(noDecision)));

        String noEnvelope = ACCEPTED_BODY.replace("\"envelope\":", "\"envelope_removed\":");
        assertThrows(NotificationDecodeException.class,
                () -> NotifyDecoder.decode(ACCEPTED_SUBJECT, bytes(noEnvelope)));
    }

    @Test
    void requiresFaultFields() {
        String noReason = FAULT_BODY.replace("\"reason\": \"runtime_error\", ", "");
        assertThrows(NotificationDecodeException.class,
                () -> NotifyDecoder.decode("x.events.fault", bytes(noReason)));

        String noRecipient = FAULT_BODY.replace("\"recipient\": \"rcpt@example.com\",", "");
        assertThrows(NotificationDecodeException.class,
                () -> NotifyDecoder.decode("x.events.fault", bytes(noRecipient)));
    }

    private static byte[] bytes(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }
}
