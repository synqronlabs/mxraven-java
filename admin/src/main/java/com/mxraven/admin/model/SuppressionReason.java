package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Reason an address is suppressed. */
public enum SuppressionReason {
    /** The address unsubscribed. */
    UNSUBSCRIBE("unsubscribe"),
    /** The address bounced. */
    BOUNCE("bounce");

    private final String wire;

    SuppressionReason(String wire) {
        this.wire = wire;
    }

    /**
     * The {@code snake_case} value used on the wire.
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
