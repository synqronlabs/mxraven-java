package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Allow-listed additive analytics metric. */
public enum MailAnalyticsMetric {
    /** Egress tasks. */
    EGRESS_TASKS("egress_tasks"),
    /** Egress data bytes. */
    DATA_BYTES("data_bytes"),
    /** Billable units. */
    BILLABLE_UNITS("billable_units"),
    /** Tasks with a terminal outcome. */
    TERMINAL_TASKS("terminal_tasks"),
    /** Tasks that succeeded. */
    SUCCEEDED_TASKS("succeeded_tasks"),
    /** Tasks that failed. */
    FAILED_TASKS("failed_tasks"),
    /** Tasks that expired. */
    EXPIRED_TASKS("expired_tasks"),
    /** Deferred delivery events. */
    DEFERRED_EVENTS("deferred_events"),
    /** SMTP responses. */
    SMTP_RESPONSES("smtp_responses"),
    /** Tasks with SMTP failures. */
    SMTP_FAILURE_TASKS("smtp_failure_tasks"),
    /** Application statuses. */
    APPLICATION_STATUSES("application_statuses"),
    /** Tasks with application failures. */
    APPLICATION_FAILURE_TASKS("application_failure_tasks"),
    /** Feedback events. */
    FEEDBACK_EVENTS("feedback_events"),
    /** Suppressions applied. */
    SUPPRESSIONS_APPLIED("suppressions_applied"),
    /** Tasks that were suppressed. */
    SUPPRESSED_TASKS("suppressed_tasks"),
    /** Forwarding events. */
    FORWARDING_EVENTS("forwarding_events"),
    /** Forwarding suppressions. */
    FORWARDING_SUPPRESSIONS("forwarding_suppressions");

    private final String wire;

    MailAnalyticsMetric(String wire) {
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
