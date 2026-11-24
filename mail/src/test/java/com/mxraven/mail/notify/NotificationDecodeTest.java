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
                "terminal_action": "TERMINAL_ACTION_TYPE_DELIVER",
                "used_listener_default": false,
                "script_revision_id": "11111111-1111-1111-1111-111111111111",
                "script_revision": 3,
                "intents": [{"kind": "header.set", "header": "X-Test", "value": "1"}],
                "skipped": [{"intent": {"kind": "forward.smtp", "ref": "fwd-1"}, "reason": "cover"}],
                "log": ["checked"],
                "http_calls": [{"endpoint": "risk", "status": 200, "duration_ms": 4,
                                "request_bytes": 2, "response_bytes": 8}]
              },
              "verdicts": {"action": "no action", "score": 1.5, "required_score": 5,
                           "is_spam": false, "has_malware": false, "malware_names": [],
                           "is_skipped": false, "error": ""},
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

    private static final String FAULT_BODY = """
            {
              "tenant_id": "tenant-1",
              "listener_id": "listener-1",
              "message_ref": "message-ref-1",
              "occurred_at_utc": 1700000002,
              "script_revision_id": "rev-1",
              "script_revision": 4,
              "mail_from": "sender@example.com",
              "recipient": "rcpt@example.com",
              "smtp_response": "451 4.3.0 routing script fault",
              "fault": {"reason": "runtime_error", "message": "boom", "line": 7},
              "log": ["started"],
              "http_calls": [{"endpoint": "risk", "status": 200, "duration_ms": 3,
                              "request_bytes": 1, "response_bytes": 2, "cached": true}],
              "suppressed_faults": 5
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
    void toleratesTheLegacyEventTypeField() {
        assertInstanceOf(AcceptedNotification.class,
                NotifyDecoder.decode(ACCEPTED_SUBJECT, bytes(ACCEPTED_BODY)));
        assertInstanceOf(AttemptNotification.class,
                NotifyDecoder.decode(ATTEMPT_SUBJECT, bytes(S3_ATTEMPT_BODY)));
    }

    @Test
    void decodesSampledFaultEvents() {
        NotificationEvent event = NotifyDecoder.decode(
                "tenant.tenant-1.listener.listener-1.events.fault", bytes(FAULT_BODY));

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
    void faultEventsFallBackToTheBodyShapeAndRequireTheirFields() {
        assertInstanceOf(FaultNotification.class,
                NotifyDecoder.decode("", bytes(FAULT_BODY)));
        assertInstanceOf(FaultNotification.class,
                NotifyDecoder.decode("unknown", bytes(FAULT_BODY)));

        String noReason = FAULT_BODY.replace("\"reason\": \"runtime_error\", ", "");
        assertThrows(NotificationDecodeException.class,
                () -> NotifyDecoder.decode("x.events.fault", bytes(noReason)));

        String noRecipient = FAULT_BODY.replace("\"recipient\": \"rcpt@example.com\",", "");
        assertThrows(NotificationDecodeException.class,
                () -> NotifyDecoder.decode("x.events.fault", bytes(noRecipient)));
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

    @Test
    void requiresARoutingDecisionAndEnvelopeOnAcceptedEvents() {
        String noDecision = ACCEPTED_BODY.replace("\"routing_decision\":", "\"decision_removed\":");
        assertThrows(NotificationDecodeException.class,
                () -> NotifyDecoder.decode(ACCEPTED_SUBJECT, bytes(noDecision)));

        String noEnvelope = ACCEPTED_BODY.replace("\"envelope\":", "\"envelope_removed\":");
        assertThrows(NotificationDecodeException.class,
                () -> NotifyDecoder.decode(ACCEPTED_SUBJECT, bytes(noEnvelope)));
    }

    private static byte[] bytes(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }
}
