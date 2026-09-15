package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Ledger dimension used to group an analytics metric. */
public enum MailAnalyticsDimension {
    NONE("none"),
    LISTENER("listener"),
    DIRECTION("direction"),
    STREAM("stream"),
    CHANNEL("channel"),
    BILLABLE_METRIC("billable_metric"),
    OUTCOME("outcome"),
    SMTP_RESPONSE_CLASS("smtp_response_class"),
    SMTP_STATUS_CODE("smtp_status_code"),
    ENHANCED_STATUS_CODE("enhanced_status_code"),
    APPLICATION_STATUS_CODE("application_status_code"),
    SENDER_DOMAIN("sender_domain"),
    RECIPIENT_DOMAIN("recipient_domain"),
    FEEDBACK_TYPE("feedback_type"),
    SUPPRESSION_REASON("suppression_reason"),
    SUPPRESSION_SCOPE("suppression_scope"),
    SUPPRESSION_FEEDBACK_COUNT("suppression_feedback_count"),
    FORWARDING_OUTCOME("forwarding_outcome"),
    FORWARDING_SIGNAL("forwarding_signal"),
    TASK_SIZE("task_size");

    private final String wire;

    MailAnalyticsDimension(String wire) {
        this.wire = wire;
    }

    /** The {@code snake_case} value used on the wire. */
    @JsonValue
    public String wire() {
        return wire;
    }

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
