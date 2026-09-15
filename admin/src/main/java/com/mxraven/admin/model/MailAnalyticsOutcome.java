package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Terminal outcome in analytics latency data. */
public enum MailAnalyticsOutcome {
    SUCCEEDED("succeeded"),
    FAILED("failed"),
    EXPIRED("expired"),
    SUPPRESSED("suppressed");

    private final String wire;

    MailAnalyticsOutcome(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

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
