package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Mail stream a listener serves. */
public enum StreamType {
    TRANSACTIONAL("transactional"),
    MARKETING("marketing"),
    SYSTEM("system");

    private final String wire;

    StreamType(String wire) {
        this.wire = wire;
    }

    /** The {@code snake_case} value used on the wire. */
    @JsonValue
    public String wire() {
        return wire;
    }

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
