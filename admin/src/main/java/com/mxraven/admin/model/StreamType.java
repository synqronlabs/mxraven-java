package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Mail stream a listener serves. */
public enum StreamType {
    /** Transactional mail stream. */
    TRANSACTIONAL("transactional"),
    /** Marketing mail stream. */
    MARKETING("marketing"),
    /** System mail stream. */
    SYSTEM("system");

    private final String wire;

    StreamType(String wire) {
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
    public static StreamType fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (StreamType candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown StreamType value: " + value);
    }
}
