package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Timezone used by mail analytics. */
public enum MailAnalyticsTimezone {
    /** Coordinated Universal Time. */
    UTC("UTC");

    private final String wire;

    MailAnalyticsTimezone(String wire) {
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
     * Resolves the constant for a wire value.
     *
     * @param value wire value, or {@code null}
     * @return the matching constant, or {@code null} when {@code value} is {@code null}
     * @throws IllegalArgumentException if the value is unknown
     */
    @JsonCreator
    public static MailAnalyticsTimezone fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (MailAnalyticsTimezone candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown MailAnalyticsTimezone value: " + value);
    }
}
