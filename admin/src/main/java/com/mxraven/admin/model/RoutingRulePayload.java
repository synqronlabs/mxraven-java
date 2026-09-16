package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Action-specific payload for a {@link RoutingRuleAction}.
 *
 * <p>The contract is a discriminated union keyed on the action type, but each action expects a
 * specific payload shape. Prefer the {@link RoutingRuleAction} factories, which pair the kind and
 * payload; the factories here are public for direct use. All factories validate locally to mirror
 * the control plane.
 */
public final class RoutingRulePayload {
    private static final Pattern RESOURCE_REF = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9._-]*$");
    private static final Pattern UUID = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    private static final Pattern ENHANCED_STATUS = Pattern.compile("^5\\.\\d+\\.\\d+$");
    private static final Pattern MAILBOX = Pattern.compile("^[^\\s<>@]+@[^\\s<>@.]+(?:\\.[^\\s<>@.]+)+$");

    private final RoutingRuleActionKind kind;
    private final Map<String, Object> values;

    private RoutingRulePayload(RoutingRuleActionKind kind, Map<String, Object> values) {
        this.kind = kind;
        this.values = Collections.unmodifiableMap(new LinkedHashMap<>(values));
    }

    /**
     * Wraps a raw free-form payload. Prefer the typed factories.
     *
     * @param values raw payload values
     * @return a payload wrapping the values
     */
    @JsonCreator
    public static RoutingRulePayload of(Map<String, Object> values) {
        return new RoutingRulePayload(null, values == null ? Java8.map() : values);
    }

    /**
     * Returns the raw payload values.
     *
     * @return the raw payload values
     */
    @JsonValue
    public Map<String, Object> values() {
        return values;
    }

    /**
     * The action this payload was built for, or {@code null} when wrapped from raw JSON.
     *
     * @return the action kind, or {@code null}
     */
    public RoutingRuleActionKind kind() {
        return kind;
    }

    // --- typed reads ---------------------------------------------------------

    /**
     * Returns the {@code pool_id} value.
     *
     * @return the {@code pool_id} value, or {@code null} when absent
     */
    public String poolId() {
        return stringValue("pool_id");
    }

    /**
     * Returns the {@code relay_ref} value.
     *
     * @return the {@code relay_ref} value, or {@code null} when absent
     */
    public String relayRef() {
        return stringValue("relay_ref");
    }

    /**
     * Returns the {@code template_ref} value.
     *
     * @return the {@code template_ref} value, or {@code null} when absent
     */
    public String templateRef() {
        return stringValue("template_ref");
    }

    /**
     * Returns the {@code webhook_ref} value.
     *
     * @return the {@code webhook_ref} value, or {@code null} when absent
     */
    public String webhookRef() {
        return stringValue("webhook_ref");
    }

    /**
     * Returns the {@code destination_ref} value.
     *
     * @return the {@code destination_ref} value, or {@code null} when absent
     */
    public String destinationRef() {
        return stringValue("destination_ref");
    }

    /**
     * Returns the {@code storage_ref} value.
     *
     * @return the {@code storage_ref} value, or {@code null} when absent
     */
    public String storageRef() {
        return stringValue("storage_ref");
    }

    /**
     * Returns the {@code object_key_prefix} value.
     *
     * @return the {@code object_key_prefix} value, or {@code null} when absent
     */
    public String objectKeyPrefix() {
        return stringValue("object_key_prefix");
    }

    /**
     * Returns the {@code object_key_template} value.
     *
     * @return the {@code object_key_template} value, or {@code null} when absent
     */
    public String objectKeyTemplate() {
        return stringValue("object_key_template");
    }

    /**
     * Returns the {@code audit_reason} value.
     *
     * @return the {@code audit_reason} value, or {@code null} when absent
     */
    public String auditReason() {
        return stringValue("audit_reason");
    }

    /**
     * Returns the {@code enhanced_status_code} value.
     *
     * @return the {@code enhanced_status_code} value, or {@code null} when absent
     */
    public String enhancedStatusCode() {
        return stringValue("enhanced_status_code");
    }

    /**
     * Returns the {@code message} value.
     *
     * @return the {@code message} value, or {@code null} when absent
     */
    public String message() {
        return stringValue("message");
    }

    /**
     * Returns the {@code smtp_status_code} value, or {@code null} when absent.
     *
     * @return the {@code smtp_status_code} value, or {@code null} when absent
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
     * Creates a deliver-to-dedicated-pool payload.
     *
     * @param poolId identifier of the dedicated IP pool
     * @return the payload
     * @throws IllegalArgumentException if {@code poolId} is not a UUID
     */
    public static RoutingRulePayload deliverDedicated(String poolId) {
        String value = require(poolId, "pool_id");
        if (!UUID.matcher(value).matches()) {
            throw new IllegalArgumentException("pool_id must be a valid UUID");
        }
        return of(RoutingRuleActionKind.DELIVER_DEDICATED, Java8.map("pool_id", value));
    }

    /**
     * Creates a default delivery payload.
     *
     * @return the payload
     */
    public static RoutingRulePayload deliver() {
        return of(RoutingRuleActionKind.DELIVER, Java8.map());
    }

    /**
     * Creates a smart-host relay payload.
     *
     * @param relayRef reference of the smart host
     * @return the payload
     * @throws IllegalArgumentException if {@code relayRef} is missing or too long
     */
    public static RoutingRulePayload smartHostRelay(String relayRef) {
        return of(RoutingRuleActionKind.SMARTHOST_RELAY, Java8.map("relay_ref", requireMax(relayRef, "relay_ref", 100)));
    }

    /**
     * Creates a relay payload.
     *
     * @param relayRef reference of the relay
     * @return the payload
     * @throws IllegalArgumentException if {@code relayRef} is missing or too long
     */
    public static RoutingRulePayload relay(String relayRef) {
        return of(RoutingRuleActionKind.RELAY, Java8.map("relay_ref", requireMax(relayRef, "relay_ref", 100)));
    }

    /**
     * Creates an auto-reply payload.
     *
     * @param templateRef reference of the auto-reply template
     * @return the payload
     * @throws IllegalArgumentException if {@code templateRef} is missing or too long
     */
    public static RoutingRulePayload autoReply(String templateRef) {
        return of(RoutingRuleActionKind.AUTO_REPLY,
                Java8.map("template_ref", requireMax(templateRef, "template_ref", 100)));
    }

    /**
     * Creates a webhook notification payload.
     *
     * @param webhookRef reference of the webhook
     * @return the payload
     * @throws IllegalArgumentException if {@code webhookRef} is missing or too long
     */
    public static RoutingRulePayload notifyWebhook(String webhookRef) {
        return of(RoutingRuleActionKind.NOTIFY_WEBHOOK,
                Java8.map("webhook_ref", requireMax(webhookRef, "webhook_ref", 100)));
    }

    /**
     * Creates a webhook delivery payload.
     *
     * @param webhookRef reference of the webhook
     * @return the payload
     * @throws IllegalArgumentException if {@code webhookRef} is missing or too long
     */
    public static RoutingRulePayload deliverWebhook(String webhookRef) {
        return of(RoutingRuleActionKind.DELIVER_WEBHOOK,
                Java8.map("webhook_ref", requireMax(webhookRef, "webhook_ref", 100)));
    }

    /**
     * Creates an SMTP forward payload.
     *
     * @param destinationRef reference of the destination
     * @return the payload
     * @throws IllegalArgumentException if {@code destinationRef} is missing or invalid
     */
    public static RoutingRulePayload smtpForward(String destinationRef) {
        return of(RoutingRuleActionKind.SMTP_FORWARD,
                Java8.map("destination_ref", requireRef(destinationRef, "destination_ref", 100)));
    }

    /**
     * Creates a drop payload.
     *
     * @return the payload
     */
    public static RoutingRulePayload drop() {
        return of(RoutingRuleActionKind.DROP, Java8.map());
    }

    /**
     * Creates a drop payload with an audit reason.
     *
     * @param auditReason reason recorded for the drop
     * @return the payload
     * @throws IllegalArgumentException if {@code auditReason} is too long
     */
    public static RoutingRulePayload drop(String auditReason) {
        String reason = optionalMax(auditReason, "audit_reason", 1024);
        return of(RoutingRuleActionKind.DROP, reason == null ? Java8.map() : Java8.map("audit_reason", reason));
    }

    /**
     * Creates a reject payload.
     *
     * @param smtpStatusCode     SMTP status code in the 5xx range
     * @param enhancedStatusCode enhanced status code of the form {@code 5.subject.detail}
     * @param message            rejection message
     * @return the payload
     * @throws IllegalArgumentException if the status codes or message are invalid
     */
    public static RoutingRulePayload reject(int smtpStatusCode, String enhancedStatusCode, String message) {
        return reject(smtpStatusCode, enhancedStatusCode, message, null);
    }

    /**
     * Creates a reject payload with an audit reason.
     *
     * @param smtpStatusCode     SMTP status code in the 5xx range
     * @param enhancedStatusCode enhanced status code of the form {@code 5.subject.detail}
     * @param message            rejection message
     * @param auditReason        reason recorded for the rejection
     * @return the payload
     * @throws IllegalArgumentException if the status codes, message, or reason are invalid
     */
    public static RoutingRulePayload reject(int smtpStatusCode, String enhancedStatusCode, String message,
                                            String auditReason) {
        if (smtpStatusCode < 500 || smtpStatusCode > 599) {
            throw new IllegalArgumentException("smtp_status_code must be in the 5xx range");
        }
        String enhanced = require(enhancedStatusCode, "enhanced_status_code");
        if (!ENHANCED_STATUS.matcher(enhanced).matches()) {
            throw new IllegalArgumentException("enhanced_status_code must match 5.subject.detail");
        }
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("smtp_status_code", smtpStatusCode);
        payload.put("enhanced_status_code", enhanced);
        payload.put("message", requireMax(message, "message", 1024));
        String reason = optionalMax(auditReason, "audit_reason", 1024);
        if (reason != null) {
            payload.put("audit_reason", reason);
        }
        return of(RoutingRuleActionKind.REJECT, payload);
    }

    /**
     * Creates an add-recipient payload.
     *
     * @param recipients recipient mailbox addresses
     * @return the payload
     * @throws IllegalArgumentException if the recipients are empty, too many, or invalid
     */
    public static RoutingRulePayload addRecipient(List<String> recipients) {
        if (recipients == null || recipients.isEmpty()) {
            throw new IllegalArgumentException("recipients must not be empty");
        }
        if (recipients.size() > 100) {
            throw new IllegalArgumentException("recipients must contain 100 entries or fewer");
        }
        List<String> normalized = new ArrayList<>(recipients.size());
        for (int index = 0; index < recipients.size(); index++) {
            String recipient = require(recipients.get(index), "recipients[" + index + "]");
            if (recipient.length() < 3 || recipient.length() > 320
                    || !MAILBOX.matcher(recipient).matches()) {
                throw new IllegalArgumentException("recipients[" + index + "] must be a valid mailbox");
            }
            normalized.add(recipient);
        }
        return of(RoutingRuleActionKind.ADD_RECIPIENT, Java8.map("recipients", normalized));
    }

    /**
     * Creates a header-modification payload.
     *
     * @param operations header operations to apply
     * @return the payload
     * @throws IllegalArgumentException if the operations are empty, too many, or invalid
     */
    public static RoutingRulePayload modifyHeader(List<ModifyHeaderOperation> operations) {
        if (operations == null || operations.isEmpty()) {
            throw new IllegalArgumentException("operations must not be empty");
        }
        if (operations.size() > 100) {
            throw new IllegalArgumentException("operations must contain 100 entries or fewer");
        }
        List<Map<String, Object>> serialized = new ArrayList<>(operations.size());
        for (int index = 0; index < operations.size(); index++) {
            ModifyHeaderOperation operation = operations.get(index);
            if (operation == null || operation.op() == null) {
                throw new IllegalArgumentException("operation[" + index + "]: op is required");
            }
            String header = require(operation.header(), "operation[" + index + "]: header");
            validateHeaderName(header, index);
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("op", operation.op());
            entry.put("header", header);
            if (operation.op() == ModifyHeaderOp.REMOVE) {
                if (trimToNull(operation.value()) != null) {
                    throw new IllegalArgumentException("operation[" + index + "]: value must be omitted for op remove");
                }
            } else {
                entry.put("value", requireMax(operation.value(), "operation[" + index + "]: value", 998));
            }
            serialized.add(entry);
        }
        return of(RoutingRuleActionKind.MODIFY_HEADER, Java8.map("operations", serialized));
    }

    /**
     * Creates an S3 storage payload.
     *
     * @param storageRef        reference of the storage backend
     * @param objectKeyPrefix   bucket-relative key prefix ending with {@code /}
     * @param objectKeyTemplate object key template
     * @return the payload
     * @throws IllegalArgumentException if the storage reference or key configuration is invalid
     */
    public static RoutingRulePayload s3Store(String storageRef, String objectKeyPrefix, String objectKeyTemplate) {
        String storage = requireMax(storageRef, "storage_ref", 100);
        String prefix = trimToNull(objectKeyPrefix);
        String template = trimToNull(objectKeyTemplate);
        if (prefix != null && template != null) {
            throw new IllegalArgumentException("only one of object_key_prefix or object_key_template may be set");
        }
        if (prefix == null && template == null) {
            throw new IllegalArgumentException("object_key_prefix or object_key_template is required");
        }
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("storage_ref", storage);
        if (prefix != null) {
            if (prefix.startsWith("/")) {
                throw new IllegalArgumentException("object_key_prefix must be bucket-relative");
            }
            if (!prefix.endsWith("/")) {
                throw new IllegalArgumentException("object_key_prefix must end with /");
            }
            payload.put("object_key_prefix", prefix);
        } else {
            payload.put("object_key_template", template);
        }
        return of(RoutingRuleActionKind.S3_STORE, payload);
    }

    // --- helpers -------------------------------------------------------------

    private static RoutingRulePayload of(RoutingRuleActionKind kind, Map<String, Object> values) {
        return new RoutingRulePayload(kind, values);
    }

    private String stringValue(String key) {
        Object value = values.get(key);
        if (value instanceof String) {
            return (String) value;
        }
        return null;
    }

    private static void validateHeaderName(String header, int index) {
        for (int position = 0; position < header.length(); position++) {
            char character = header.charAt(position);
            if (character <= 32 || character >= 127 || character == ':') {
                throw new IllegalArgumentException("operation[" + index + "]: header is invalid");
            }
        }
    }

    private static String require(String value, String field) {
        String trimmed = trimToNull(value);
        if (trimmed == null) {
            throw new IllegalArgumentException(field + " is required");
        }
        return trimmed;
    }

    private static String requireMax(String value, String field, int maxLength) {
        String trimmed = require(value, field);
        if (trimmed.length() > maxLength) {
            throw new IllegalArgumentException(field + " must be " + maxLength + " characters or fewer");
        }
        return trimmed;
    }

    private static String requireRef(String value, String field, int maxLength) {
        String trimmed = requireMax(value, field, maxLength);
        if (!RESOURCE_REF.matcher(trimmed).matches()) {
            throw new IllegalArgumentException(field + " must start with a letter or digit and may contain "
                    + "letters, digits, dots, underscores, or hyphens");
        }
        return trimmed;
    }

    private static String optionalMax(String value, String field, int maxLength) {
        String trimmed = trimToNull(value);
        if (trimmed != null && trimmed.length() > maxLength) {
            throw new IllegalArgumentException(field + " must be " + maxLength + " characters or fewer");
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
