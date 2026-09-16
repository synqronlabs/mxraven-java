package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Action-specific payload for a listener's default terminal action.
 *
 * <p>The contract models this as free-form JSON, but each {@link TerminalActionType}
 * expects a specific shape. Use the factories rather than building a raw map:
 *
 * <pre>
 * CreateListenerRequest.builder()
 *         .displayName("Inbound")
 *         .listenerType(ListenerType.MTA)
 *         .streamType(StreamType.TRANSACTIONAL)
 *         .defaultTerminalActionType(TerminalActionType.RELAY)
 *         .defaultTerminalActionPayload(TerminalActionPayload.relay("primary"))
 *         .build();
 * </pre>
 *
 * <p>Factories validate locally to match the control plane, so malformed payloads
 * fail before a request is sent.
 */
public final class TerminalActionPayload {
    private static final Pattern RESOURCE_REF = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9._-]*$");
    private static final Pattern ENHANCED_STATUS = Pattern.compile("^\\d+\\.\\d+\\.\\d+$");
    private static final Pattern UUID = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    private final Map<String, Object> values;

    private TerminalActionPayload(Map<String, Object> values) {
        this.values = Collections.unmodifiableMap(new LinkedHashMap<>(values));
    }

    /**
     * Wrap a raw free-form payload. Prefer the typed factories.
     *
     * @param values raw payload values
     * @return a payload wrapping the values
     */
    @JsonCreator
    public static TerminalActionPayload of(Map<String, Object> values) {
        return new TerminalActionPayload(values == null ? Java8.map() : values);
    }

    /**
     * Returns the payload as a map of wire fields.
     *
     * @return the payload map
     */
    @JsonValue
    public Map<String, Object> values() {
        return values;
    }

    /**
     * Returns whether the payload is empty.
     *
     * @return {@code true} if the payload has no fields
     */
    public boolean isEmpty() {
        return values.isEmpty();
    }

    // --- typed reads ---------------------------------------------------------

    /**
     * Returns the {@code pool_id} value.
     *
     * @return the pool identifier, or {@code null} if absent
     */
    public String poolId() {
        return stringValue("pool_id");
    }

    /**
     * Returns the {@code relay_ref} value.
     *
     * @return the relay reference, or {@code null} if absent
     */
    public String relayRef() {
        return stringValue("relay_ref");
    }

    /**
     * Returns the {@code template_ref} value.
     *
     * @return the template reference, or {@code null} if absent
     */
    public String templateRef() {
        return stringValue("template_ref");
    }

    /**
     * Returns the {@code audit_reason} value.
     *
     * @return the audit reason, or {@code null} if absent
     */
    public String auditReason() {
        return stringValue("audit_reason");
    }

    /**
     * Returns the {@code enhanced_status_code} value.
     *
     * @return the enhanced status code, or {@code null} if absent
     */
    public String enhancedStatusCode() {
        return stringValue("enhanced_status_code");
    }

    /**
     * Returns the {@code message} value.
     *
     * @return the rejection message, or {@code null} if absent
     */
    public String message() {
        return stringValue("message");
    }

    /**
     * Returns the {@code smtp_status_code} value.
     *
     * @return the SMTP status code, or {@code null} if absent
     */
    public Integer smtpStatusCode() {
        Object value = values.get("smtp_status_code");
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return null;
    }

    // --- factories -----------------------------------------------------------

    /**
     * An empty payload, for actions that take none ({@code DELIVER}, {@code DROP}).
     *
     * @return an empty payload
     */
    public static TerminalActionPayload empty() {
        return of(Java8.map());
    }

    /**
     * {@code DELIVER}: shared delivery, no payload.
     *
     * @return an empty payload
     */
    public static TerminalActionPayload deliver() {
        return empty();
    }

    /**
     * {@code DELIVER_DEDICATED}: {@code pool_id} must reference a leased dedicated pool.
     *
     * @param poolId identifier of the dedicated IP pool
     * @return the payload
     * @throws IllegalArgumentException if {@code poolId} is not a UUID
     */
    public static TerminalActionPayload deliverDedicated(String poolId) {
        String value = require(poolId, "pool_id");
        if (!UUID.matcher(value).matches()) {
            throw new IllegalArgumentException("pool_id must be a valid UUID");
        }
        return of(Java8.map("pool_id", value));
    }

    /**
     * {@code SMARTHOST_RELAY}: {@code relay_ref} must reference an active relay.
     *
     * @param relayRef reference of the smart host
     * @return the payload
     * @throws IllegalArgumentException if {@code relayRef} is missing or invalid
     */
    public static TerminalActionPayload smartHostRelay(String relayRef) {
        return relay(relayRef);
    }

    /**
     * {@code RELAY}: {@code relay_ref} must reference an active relay.
     *
     * @param relayRef reference of the relay
     * @return the payload
     * @throws IllegalArgumentException if {@code relayRef} is missing or invalid
     */
    public static TerminalActionPayload relay(String relayRef) {
        return of(Java8.map("relay_ref", requireRef(relayRef, "relay_ref")));
    }

    /**
     * {@code AUTO_REPLY}: {@code template_ref} must reference an active template.
     *
     * @param templateRef reference of the auto-reply template
     * @return the payload
     * @throws IllegalArgumentException if {@code templateRef} is missing or invalid
     */
    public static TerminalActionPayload autoReply(String templateRef) {
        return of(Java8.map("template_ref", requireRef(templateRef, "template_ref")));
    }

    /**
     * {@code DROP}: no payload.
     *
     * @return an empty payload
     */
    public static TerminalActionPayload drop() {
        return empty();
    }

    /**
     * {@code DROP}: no payload with an optional audit reason.
     *
     * @param auditReason reason recorded for the drop; may be {@code null}
     * @return the payload
     */
    public static TerminalActionPayload drop(String auditReason) {
        String reason = trimToNull(auditReason);
        return reason == null ? drop() : of(Java8.map("audit_reason", reason));
    }

    /**
     * {@code REJECT}: 5xx SMTP status, enhanced status, and rejection message.
     *
     * @param smtpStatusCode     SMTP status code in the 5xx range
     * @param enhancedStatusCode enhanced status code of the form {@code 5.subject.detail}
     * @param message            rejection message
     * @return the payload
     * @throws IllegalArgumentException if the status codes or message are invalid
     */
    public static TerminalActionPayload reject(int smtpStatusCode, String enhancedStatusCode, String message) {
        return reject(smtpStatusCode, enhancedStatusCode, message, null);
    }

    /**
     * {@code REJECT}: as above, with an optional audit reason.
     *
     * @param smtpStatusCode     SMTP status code in the 5xx range
     * @param enhancedStatusCode enhanced status code of the form {@code 5.subject.detail}
     * @param message            rejection message
     * @param auditReason        reason recorded for the rejection; may be {@code null}
     * @return the payload
     * @throws IllegalArgumentException if the status codes, message, or reason are invalid
     */
    public static TerminalActionPayload reject(int smtpStatusCode, String enhancedStatusCode, String message,
                                               String auditReason) {
        if (smtpStatusCode < 500 || smtpStatusCode > 599) {
            throw new IllegalArgumentException("smtp_status_code must be in the 5xx range");
        }
        String enhanced = require(enhancedStatusCode, "enhanced_status_code");
        if (!ENHANCED_STATUS.matcher(enhanced).matches()) {
            throw new IllegalArgumentException("enhanced_status_code must use class.subject.detail format");
        }
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("smtp_status_code", smtpStatusCode);
        payload.put("enhanced_status_code", enhanced);
        payload.put("message", require(message, "message"));
        String reason = trimToNull(auditReason);
        if (reason != null) {
            payload.put("audit_reason", reason);
        }
        return of(payload);
    }

    private String stringValue(String key) {
        Object value = values.get(key);
        if (value instanceof String) {
            return (String) value;
        }
        return null;
    }

    private static String require(String value, String field) {
        String trimmed = trimToNull(value);
        if (trimmed == null) {
            throw new IllegalArgumentException(field + " is required");
        }
        return trimmed;
    }

    private static String requireRef(String value, String field) {
        String trimmed = require(value, field);
        if (!RESOURCE_REF.matcher(trimmed).matches()) {
            throw new IllegalArgumentException(field
                    + " must start with a letter or digit and may contain letters, digits, dots, underscores, or hyphens");
        }
        return trimmed;
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
