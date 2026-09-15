package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Bucket granularity for an analytics time series. */
public enum MailAnalyticsBucketGranularity {
    HOUR("hour"),
    DAY("day");

    private final String wire;

    MailAnalyticsBucketGranularity(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

    @JsonCreator
    public static MailAnalyticsBucketGranularity fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (MailAnalyticsBucketGranularity candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown MailAnalyticsBucketGranularity value: " + value);
    }
}
