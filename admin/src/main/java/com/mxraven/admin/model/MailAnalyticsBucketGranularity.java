package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Bucket granularity for an analytics time series. */
public enum MailAnalyticsBucketGranularity {
    /** Hourly buckets. */
    HOUR("hour"),
    /** Daily buckets. */
    DAY("day");

    private final String wire;

    MailAnalyticsBucketGranularity(String wire) {
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
