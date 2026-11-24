package com.mxraven.mail.notify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * Decoding of notification subjects and bodies.
 *
 * <p>The event kind is the final token of the subject. When the subject is
 * unavailable or unrecognized, the body shape is probed instead: an attempt
 * carries a {@code channel}, and an accepted event carries a
 * {@code routing_decision}.
 */
final class NotifyDecoder {

    private NotifyDecoder() {
    }

    /**
     * Decodes one notification body.
     *
     * @param subject the subject the notification was published to
     * @param body the JSON body
     * @return the decoded notification
     * @throws NotificationDecodeException when the body is not valid JSON, has
     *                                     a field of the wrong type, or cannot
     *                                     be identified
     */
    static NotificationEvent decode(String subject, byte[] body) {
        String safeSubject = subject == null ? "" : subject;
        JsonNode payload;
        try {
            payload = NotifyJson.MAPPER.readTree(body == null ? new byte[0] : body);
        } catch (IOException e) {
            throw new NotificationDecodeException(safeSubject, e);
        }
        if (payload == null || !payload.isObject()) {
            throw new NotificationDecodeException(safeSubject, "body is not a JSON object");
        }

        String kind = subjectKind(safeSubject);
        if (NotificationKind.ACCEPTED.wire().equals(kind)) {
            return decodeAccepted(payload, safeSubject);
        }
        if (NotificationKind.ATTEMPT.wire().equals(kind)) {
            return decodeAttempt(payload, safeSubject);
        }
        if (NotificationKind.FAULT.wire().equals(kind)) {
            return decodeFault(payload, safeSubject);
        }

        if (isTextual(payload.get("channel"))) {
            return decodeAttempt(payload, safeSubject);
        }
        JsonNode routingDecision = payload.get("routing_decision");
        if (routingDecision != null && !routingDecision.isNull()) {
            return decodeAccepted(payload, safeSubject);
        }
        JsonNode fault = payload.get("fault");
        if (fault != null && !fault.isNull()) {
            return decodeFault(payload, safeSubject);
        }
        throw new NotificationDecodeException(safeSubject, "unrecognized notification body");
    }

    /** Returns the final subject token, which is the event kind. */
    private static String subjectKind(String subject) {
        String trimmed = subject.trim();
        int index = trimmed.lastIndexOf('.');
        return index >= 0 ? trimmed.substring(index + 1).trim() : trimmed;
    }

    /** Decodes an accepted-message body and validates its required fields. */
    private static NotificationEvent decodeAccepted(JsonNode payload, String subject) {
        require(payload, subject, "task_id", "tenant_id", "listener_id", "attempt",
                "accepted_at_utc", "occurred_at_utc", "routing_decision", "envelope");
        if (!isTextual(payload.get("task_id")) || payload.get("task_id").textValue().isEmpty()) {
            throw fieldTypeError(subject, "task_id", "a non-empty string");
        }
        if (!isTextual(payload.get("tenant_id")) || payload.get("tenant_id").textValue().isEmpty()) {
            throw fieldTypeError(subject, "tenant_id", "a non-empty string");
        }
        if (!isTextual(payload.get("listener_id")) || payload.get("listener_id").textValue().isEmpty()) {
            throw fieldTypeError(subject, "listener_id", "a non-empty string");
        }
        if (!isNumber(payload.get("attempt")) || payload.get("attempt").longValue() < 1) {
            throw fieldTypeError(subject, "attempt", "a positive number");
        }

        JsonNode decision = payload.get("routing_decision");
        if (!decision.isObject()) {
            throw fieldTypeError(subject, "routing_decision", "an object");
        }
        require(decision, subject, "terminal_action", "used_listener_default");
        if (!isTextual(decision.get("terminal_action")) || decision.get("terminal_action").textValue().isEmpty()) {
            throw fieldTypeError(subject, "routing_decision.terminal_action", "a non-empty string");
        }
        JsonNode envelope = payload.get("envelope");
        if (!envelope.isObject()) {
            throw fieldTypeError(subject, "envelope", "an object");
        }
        require(envelope, subject, "mail_from");

        AcceptedMessage accepted;
        try {
            accepted = NotifyJson.MAPPER.treeToValue(payload, AcceptedMessage.class);
        } catch (JsonProcessingException e) {
            throw new NotificationDecodeException(subject, e.getOriginalMessage());
        }
        if (accepted == null) {
            throw new NotificationDecodeException(subject, "body is not a JSON object");
        }
        for (int index = 0; index < accepted.headers().size(); index++) {
            HeaderField header = accepted.headers().get(index);
            if (header.name() == null || header.name().isEmpty()) {
                throw fieldTypeError(subject, "headers[" + index + "].name", "a non-empty string");
            }
        }
        return new AcceptedNotification(accepted);
    }

    /** Decodes an attempt body and validates its required fields. */
    private static NotificationEvent decodeAttempt(JsonNode source, String subject) {
        require(source, subject, "channel", "task_id", "tenant_id", "listener_id",
                "status", "attempt", "occurred_at_utc");
        if (!isTextual(source.get("channel")) || source.get("channel").textValue().isEmpty()) {
            throw fieldTypeError(subject, "channel", "a non-empty string");
        }
        if (!isTextual(source.get("task_id")) || source.get("task_id").textValue().isEmpty()) {
            throw fieldTypeError(subject, "task_id", "a non-empty string");
        }
        if (!isTextual(source.get("tenant_id")) || source.get("tenant_id").textValue().isEmpty()) {
            throw fieldTypeError(subject, "tenant_id", "a non-empty string");
        }
        if (!isTextual(source.get("listener_id")) || source.get("listener_id").textValue().isEmpty()) {
            throw fieldTypeError(subject, "listener_id", "a non-empty string");
        }
        if (!isTextual(source.get("status")) || source.get("status").textValue().isEmpty()) {
            throw fieldTypeError(subject, "status", "a non-empty string");
        }
        if (!isNumber(source.get("attempt")) || source.get("attempt").longValue() < 1) {
            throw fieldTypeError(subject, "attempt", "a positive number");
        }

        try {
            AttemptEvent attempt = NotifyJson.MAPPER.treeToValue(source, AttemptEvent.class);
            if (attempt == null) {
                throw new NotificationDecodeException(subject, "body is not a JSON object");
            }
            return new AttemptNotification(attempt);
        } catch (JsonProcessingException e) {
            throw new NotificationDecodeException(subject, e.getOriginalMessage());
        } catch (IllegalArgumentException e) {
            throw new NotificationDecodeException(subject, e.getMessage());
        }
    }

    /** Decodes a routing-script fault body and validates its required fields. */
    private static NotificationEvent decodeFault(JsonNode payload, String subject) {
        require(payload, subject, "tenant_id", "listener_id", "message_ref",
                "occurred_at_utc", "script_revision_id", "script_revision", "mail_from",
                "recipient", "smtp_response", "fault", "log", "suppressed_faults");
        requireNonEmptyText(payload, subject, "tenant_id");
        requireNonEmptyText(payload, subject, "listener_id");
        requireNonEmptyText(payload, subject, "message_ref");
        requireNonEmptyText(payload, subject, "recipient");

        JsonNode fault = payload.get("fault");
        if (!fault.isObject()) {
            throw fieldTypeError(subject, "fault", "an object");
        }
        require(fault, subject, "reason", "message");
        if (!isTextual(fault.get("reason")) || fault.get("reason").textValue().isEmpty()) {
            throw fieldTypeError(subject, "fault.reason", "a non-empty string");
        }
        if (!isTextual(fault.get("message")) || fault.get("message").textValue().isEmpty()) {
            throw fieldTypeError(subject, "fault.message", "a non-empty string");
        }

        try {
            FaultEvent event = NotifyJson.MAPPER.treeToValue(payload, FaultEvent.class);
            if (event == null) {
                throw new NotificationDecodeException(subject, "body is not a JSON object");
            }
            return new FaultNotification(event);
        } catch (JsonProcessingException e) {
            throw new NotificationDecodeException(subject, e.getOriginalMessage());
        } catch (IllegalArgumentException e) {
            throw new NotificationDecodeException(subject, e.getMessage());
        }
    }

    private static void requireNonEmptyText(JsonNode source, String subject, String field) {
        if (!isTextual(source.get(field)) || source.get(field).textValue().isEmpty()) {
            throw fieldTypeError(subject, field, "a non-empty string");
        }
    }

    private static void require(JsonNode source, String subject, String... fields) {
        for (String field : fields) {
            JsonNode value = source.get(field);
            if (value == null || value.isNull()) {
                throw fieldTypeError(subject, field, "present");
            }
        }
    }

    private static boolean isTextual(JsonNode value) {
        return value != null && value.isTextual();
    }

    private static boolean isNumber(JsonNode value) {
        return value != null && value.isNumber();
    }

    private static NotificationDecodeException fieldTypeError(String subject, String field, String want) {
        return new NotificationDecodeException(subject, "field \"" + field + "\" must be " + want);
    }
}
