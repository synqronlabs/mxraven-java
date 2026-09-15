package com.mxraven.admin.model;

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
 * <pre>{@code
 * CreateListenerRequest.builder()
 *         .displayName("Inbound")
 *         .listenerType(ListenerType.MTA)
 *         .streamType(StreamType.TRANSACTIONAL)
 *         .defaultTerminalActionType(TerminalActionType.RELAY)
 *         .defaultTerminalActionPayload(TerminalActionPayload.relay("primary"))
 *         .build();
 * }</pre>
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

    /** Wrap a raw free-form payload. Prefer the typed factories. */
    @JsonCreator
    public static TerminalActionPayload of(Map<String, Object> values) {
        return new TerminalActionPayload(values == null ? Map.of() : values);
    }

    @JsonValue
    public Map<String, Object> values() {
        return values;
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    // --- typed reads ---------------------------------------------------------

    public String poolId() {
        return stringValue("pool_id");
    }

    public String relayRef() {
        return stringValue("relay_ref");
    }

    public String templateRef() {
        return stringValue("template_ref");
    }

    public String auditReason() {
        return stringValue("audit_reason");
    }

    public String enhancedStatusCode() {
        return stringValue("enhanced_status_code");
    }

    public String message() {
        return stringValue("message");
    }

    public Integer smtpStatusCode() {
        Object value = values.get("smtp_status_code");
        return value instanceof Number number ? number.intValue() : null;
    }

    // --- factories -----------------------------------------------------------

    /** An empty payload, for actions that take none ({@code DELIVER}, {@code DROP}). */
    public static TerminalActionPayload empty() {
        return of(Map.of());
    }

    /** {@code DELIVER}: shared delivery, no payload. */
    public static TerminalActionPayload deliver() {
        return empty();
    }

    /** {@code DELIVER_DEDICATED}: {@code pool_id} must reference a leased dedicated pool. */
    public static TerminalActionPayload deliverDedicated(String poolId) {
        String value = require(poolId, "pool_id");
        if (!UUID.matcher(value).matches()) {
            throw new IllegalArgumentException("pool_id must be a valid UUID");
        }
        return of(Map.of("pool_id", value));
    }

    /** {@code SMARTHOST_RELAY}: {@code relay_ref} must reference an active relay. */
    public static TerminalActionPayload smartHostRelay(String relayRef) {
        return relay(relayRef);
    }

    /** {@code RELAY}: {@code relay_ref} must reference an active relay. */
    public static TerminalActionPayload relay(String relayRef) {
        return of(Map.of("relay_ref", requireRef(relayRef, "relay_ref")));
    }

    /** {@code AUTO_REPLY}: {@code template_ref} must reference an active template. */
    public static TerminalActionPayload autoReply(String templateRef) {
        return of(Map.of("template_ref", requireRef(templateRef, "template_ref")));
    }

    /** {@code DROP}: no payload. */
    public static TerminalActionPayload drop() {
        return empty();
    }

    /** {@code DROP}: no payload with an optional audit reason. */
    public static TerminalActionPayload drop(String auditReason) {
        String reason = trimToNull(auditReason);
        return reason == null ? drop() : of(Map.of("audit_reason", reason));
    }

    /** {@code REJECT}: 5xx SMTP status, enhanced status, and rejection message. */
    public static TerminalActionPayload reject(int smtpStatusCode, String enhancedStatusCode, String message) {
        return reject(smtpStatusCode, enhancedStatusCode, message, null);
    }

    /** {@code REJECT}: as above, with an optional audit reason. */
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
        return value instanceof String text ? text : null;
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
