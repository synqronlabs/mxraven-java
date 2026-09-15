package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Reason an address is suppressed. */
public enum SuppressionReason {
    UNSUBSCRIBE("unsubscribe"),
    BOUNCE("bounce");

    private final String wire;

    SuppressionReason(String wire) {
        this.wire = wire;
    }

    /** The {@code snake_case} value used on the wire. */
    @JsonValue
    public String wire() {
        return wire;
    }

    @JsonCreator
    public static SuppressionReason fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (SuppressionReason candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown SuppressionReason value: " + value);
    }
}
