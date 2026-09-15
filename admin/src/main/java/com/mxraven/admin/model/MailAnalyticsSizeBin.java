package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Message-size bin in analytics latency data. */
public enum MailAnalyticsSizeBin {
    UNDER_10_KIB("under_10_kib"),
    FROM_10_TO_100_KIB("from_10_to_100_kib"),
    FROM_100_KIB_TO_1_MIB("from_100_kib_to_1_mib"),
    FROM_1_TO_5_MIB("from_1_to_5_mib"),
    FROM_5_TO_10_MIB("from_5_to_10_mib"),
    FROM_10_TO_25_MIB("from_10_to_25_mib"),
    FROM_25_TO_50_MIB("from_25_to_50_mib"),
    OVER_50_MIB("over_50_mib");

    private final String wire;

    MailAnalyticsSizeBin(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

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
