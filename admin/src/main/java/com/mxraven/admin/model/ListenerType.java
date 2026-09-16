package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Listener kind. */
public enum ListenerType {
    /** Submission listener. */
    SUBMISSION("submission"),
    /** MTA listener. */
    MTA("mta");

    private final String wire;

    ListenerType(String wire) {
        this.wire = wire;
    }

    /**
     * The {@code snake_case} value used on the wire.
     *
     * @return the {@code snake_case} value used on the wire
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
