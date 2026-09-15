package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Allow-listed additive analytics metric. */
public enum MailAnalyticsMetric {
    EGRESS_TASKS("egress_tasks"),
    DATA_BYTES("data_bytes"),
    BILLABLE_UNITS("billable_units"),
    TERMINAL_TASKS("terminal_tasks"),
    SUCCEEDED_TASKS("succeeded_tasks"),
    FAILED_TASKS("failed_tasks"),
    EXPIRED_TASKS("expired_tasks"),
    DEFERRED_EVENTS("deferred_events"),
    SMTP_RESPONSES("smtp_responses"),
    SMTP_FAILURE_TASKS("smtp_failure_tasks"),
    APPLICATION_STATUSES("application_statuses"),
    APPLICATION_FAILURE_TASKS("application_failure_tasks"),
    FEEDBACK_EVENTS("feedback_events"),
    SUPPRESSIONS_APPLIED("suppressions_applied"),
    SUPPRESSED_TASKS("suppressed_tasks"),
    FORWARDING_EVENTS("forwarding_events"),
    FORWARDING_SUPPRESSIONS("forwarding_suppressions");

    private final String wire;

    MailAnalyticsMetric(String wire) {
        this.wire = wire;
    }

    /** The {@code snake_case} value used on the wire. */
    @JsonValue
    public String wire() {
        return wire;
    }

    @JsonCreator
    public static MailAnalyticsMetric fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (MailAnalyticsMetric candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown MailAnalyticsMetric value: " + value);
    }
}
