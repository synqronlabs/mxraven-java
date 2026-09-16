package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Message-size bin in analytics latency data. */
public enum MailAnalyticsSizeBin {
    /** Messages smaller than 10 KiB. */
    UNDER_10_KIB("under_10_kib"),
    /** Messages from 10 KiB up to 100 KiB. */
    FROM_10_TO_100_KIB("from_10_to_100_kib"),
    /** Messages from 100 KiB up to 1 MiB. */
    FROM_100_KIB_TO_1_MIB("from_100_kib_to_1_mib"),
    /** Messages from 1 MiB up to 5 MiB. */
    FROM_1_TO_5_MIB("from_1_to_5_mib"),
    /** Messages from 5 MiB up to 10 MiB. */
    FROM_5_TO_10_MIB("from_5_to_10_mib"),
    /** Messages from 10 MiB up to 25 MiB. */
    FROM_10_TO_25_MIB("from_10_to_25_mib"),
    /** Messages from 25 MiB up to 50 MiB. */
    FROM_25_TO_50_MIB("from_25_to_50_mib"),
    /** Messages larger than 50 MiB. */
    OVER_50_MIB("over_50_mib");

    private final String wire;

    MailAnalyticsSizeBin(String wire) {
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
    public static MailAnalyticsSizeBin fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (MailAnalyticsSizeBin candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown MailAnalyticsSizeBin value: " + value);
    }
}
