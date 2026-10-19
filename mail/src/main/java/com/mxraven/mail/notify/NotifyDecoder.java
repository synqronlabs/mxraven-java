package com.mxraven.mail.notify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.mxraven.mail.webhook.InboundEmail;
import com.mxraven.mail.webhook.StatusOutcome;

import java.io.IOException;

/**
 * Decoding of notification subjects and bodies.
 *
 * <p>The event kind is the final token of the subject. When the subject is
 * unavailable or unrecognized, the body shape is probed instead.
 */
final class NotifyDecoder {

    private NotifyDecoder() {
    }

    /**
     * Decodes one notification body.
     *
     * @param subject the subject the notification was published to
     * @param body    the JSON body
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

        JsonNode eventType = payload.get("event_type");
        if (eventType != null && eventType.isTextual() && "inbound_email".equals(eventType.textValue())) {
            return decodeAccepted(payload, safeSubject);
        }
        JsonNode channel = payload.get("channel");
        if (channel != null && channel.isTextual() && !channel.textValue().isEmpty()) {
            return decodeAttempt(payload, safeSubject);
        }
        throw new NotificationDecodeException(safeSubject, "unrecognized notification body");
    }

    /** Returns the final subject token, which is the event kind. */
    private static String subjectKind(String subject) {
        String trimmed = subject.trim();
        int index = trimmed.lastIndexOf('.');
        return index >= 0 ? trimmed.substring(index + 1).trim() : trimmed;
    }

    /**
     * Decodes an accepted-message body. The payload is the webhook inbound-email
     * model, so it is reused verbatim.
     */
    private static NotificationEvent decodeAccepted(JsonNode payload, String subject) {
        try {
            InboundEmail accepted = NotifyJson.MAPPER.treeToValue(payload, InboundEmail.class);
            if (accepted == null) {
                throw new NotificationDecodeException(subject, "body is not a JSON object");
            }
            return new AcceptedNotification(accepted);
        } catch (JsonProcessingException e) {
            throw new NotificationDecodeException(subject, e.getOriginalMessage());
        }
    }

    /** Decodes an attempt body into its typed model. */
    private static NotificationEvent decodeAttempt(JsonNode source, String subject) {
        String channelWire = requiredText(source, "channel", subject);
        String statusWire = requiredText(source, "status", subject);
        AttemptChannel channel;
        StatusOutcome status;
        try {
            channel = AttemptChannel.fromWire(channelWire);
        } catch (IllegalArgumentException e) {
            throw new NotificationDecodeException(subject, "unknown channel \"" + channelWire + "\"");
        }
        try {
            status = StatusOutcome.fromWire(statusWire);
        } catch (IllegalArgumentException e) {
            throw new NotificationDecodeException(subject, "unknown status \"" + statusWire + "\"");
        }

        AttemptEvent attempt = new AttemptEvent(
                optionalText(source, "event_type", subject),
                channel,
                requiredText(source, "task_id", subject),
                requiredText(source, "tenant_id", subject),
                requiredText(source, "listener_id", subject),
                optionalText(source, "correlation_id", subject),
                status,
                requiredLong(source, "attempt", subject),
                optionalLong(source, "accepted_at_utc", subject),
                requiredLong(source, "occurred_at_utc", subject),
                optionalText(source, "source_ip", subject),
                optionalText(source, "destination_domain", subject),
                optionalText(source, "remote_host", subject),
                optionalInteger(source, "smtp_code", subject),
                optionalText(source, "enhanced_status_code", subject),
                optionalText(source, "reason", subject),
                optionalText(source, "remote_response", subject),
                optionalText(source, "correlation_task_id", subject),
                optionalText(source, "storage_ref", subject),
                optionalText(source, "bucket_name", subject),
                optionalText(source, "object_key", subject),
                optionalText(source, "endpoint_host", subject),
                optionalText(source, "error_code", subject),
                optionalInteger(source, "status_code", subject),
                optionalText(source, "message", subject),
                optionalLong(source, "next_retry_at_utc", subject),
                optionalLong(source, "retry_after_ms", subject),
                optionalText(source, "target_url_host", subject),
                optionalText(source, "webhook_ref", subject),
                optionalText(source, "signing_key_kid", subject),
                optionalLong(source, "raw_token_ttl_seconds", subject));
        return new AttemptNotification(attempt);
    }

    private static String requiredText(JsonNode source, String field, String subject) {
        JsonNode value = source.get(field);
        if (value == null || !value.isTextual()) {
            throw fieldTypeError(subject, field, "a string");
        }
        return value.textValue();
    }

    private static String optionalText(JsonNode source, String field, String subject) {
        JsonNode value = source.get(field);
        if (value == null || value.isNull()) {
            return null;
        }
        if (!value.isTextual()) {
            throw fieldTypeError(subject, field, "a string");
        }
        return value.textValue();
    }

    private static long requiredLong(JsonNode source, String field, String subject) {
        JsonNode value = source.get(field);
        if (value == null || !value.isNumber()) {
            throw fieldTypeError(subject, field, "a number");
        }
        return value.longValue();
    }

    private static Long optionalLong(JsonNode source, String field, String subject) {
        JsonNode value = source.get(field);
        if (value == null || value.isNull()) {
            return null;
        }
        if (!value.isNumber()) {
            throw fieldTypeError(subject, field, "a number");
        }
        return value.longValue();
    }

    private static Integer optionalInteger(JsonNode source, String field, String subject) {
        JsonNode value = source.get(field);
        if (value == null || value.isNull()) {
            return null;
        }
        if (!value.isNumber()) {
            throw fieldTypeError(subject, field, "a number");
        }
        return value.intValue();
    }

    private static NotificationDecodeException fieldTypeError(String subject, String field, String want) {
        return new NotificationDecodeException(subject, "field \"" + field + "\" must be " + want);
    }
}
