package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Timezone used by mail analytics. */
public enum MailAnalyticsTimezone {
    UTC("UTC");

    private final String wire;

    MailAnalyticsTimezone(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

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
