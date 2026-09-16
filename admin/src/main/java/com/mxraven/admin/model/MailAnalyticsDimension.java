package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Ledger dimension used to group an analytics metric. */
public enum MailAnalyticsDimension {
    /** No grouping dimension. */
    NONE("none"),
    /** Groups by listener. */
    LISTENER("listener"),
    /** Groups by message direction. */
    DIRECTION("direction"),
    /** Groups by message stream. */
    STREAM("stream"),
    /** Groups by submission or MTA channel. */
    CHANNEL("channel"),
    /** Groups by billable metric. */
    BILLABLE_METRIC("billable_metric"),
    /** Groups by terminal outcome. */
    OUTCOME("outcome"),
    /** Groups by SMTP response class. */
    SMTP_RESPONSE_CLASS("smtp_response_class"),
    /** Groups by SMTP status code. */
    SMTP_STATUS_CODE("smtp_status_code"),
    /** Groups by enhanced status code. */
    ENHANCED_STATUS_CODE("enhanced_status_code"),
    /** Groups by application status code. */
    APPLICATION_STATUS_CODE("application_status_code"),
    /** Groups by sender domain. */
    SENDER_DOMAIN("sender_domain"),
    /** Groups by recipient domain. */
    RECIPIENT_DOMAIN("recipient_domain"),
    /** Groups by feedback type. */
    FEEDBACK_TYPE("feedback_type"),
    /** Groups by suppression reason. */
    SUPPRESSION_REASON("suppression_reason"),
    /** Groups by suppression scope. */
    SUPPRESSION_SCOPE("suppression_scope"),
    /** Groups by suppression feedback count. */
    SUPPRESSION_FEEDBACK_COUNT("suppression_feedback_count"),
    /** Groups by forwarding outcome. */
    FORWARDING_OUTCOME("forwarding_outcome"),
    /** Groups by forwarding signal. */
    FORWARDING_SIGNAL("forwarding_signal"),
    /** Groups by task size. */
    TASK_SIZE("task_size");

    private final String wire;

    MailAnalyticsDimension(String wire) {
        this.wire = wire;
    }

    /**
     * The {@code snake_case} value used on the wire.
     *
     * @return the {@code snake_case} value used on the wire
     */
    @JsonValue
    public String wire() {
        return wire;
    }

    /**
     * Resolves the constant matching a wire value.
     *
     * @param value the wire value; may be {@code null}
     * @return the matching constant, or {@code null} when {@code value} is {@code null}
     * @throws IllegalArgumentException if no constant matches
     */
    @JsonCreator
    public static MailAnalyticsDimension fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (MailAnalyticsDimension candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown MailAnalyticsDimension value: " + value);
    }
}
