package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Terminal outcome in analytics latency data. */
public enum MailAnalyticsOutcome {
    /** Task succeeded. */
    SUCCEEDED("succeeded"),
    /** Task failed. */
    FAILED("failed"),
    /** Task expired. */
    EXPIRED("expired"),
    /** Task was suppressed. */
    SUPPRESSED("suppressed");

    private final String wire;

    MailAnalyticsOutcome(String wire) {
        this.wire = wire;
    }

    /**
     * The wire value.
     *
     * @return the wire value
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
    public static MailAnalyticsOutcome fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (MailAnalyticsOutcome candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown MailAnalyticsOutcome value: " + value);
    }
}
