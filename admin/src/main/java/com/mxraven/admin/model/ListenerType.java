package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Listener kind. */
public enum ListenerType {
    SUBMISSION("submission"),
    MTA("mta");

    private final String wire;

    ListenerType(String wire) {
        this.wire = wire;
    }

    /** The {@code snake_case} value used on the wire. */
    @JsonValue
    public String wire() {
        return wire;
    }

    @JsonCreator
    public static ListenerType fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (ListenerType candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown ListenerType value: " + value);
    }
}
