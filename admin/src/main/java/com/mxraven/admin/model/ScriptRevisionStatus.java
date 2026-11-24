package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Lifecycle state of a routing-script revision. */
public enum ScriptRevisionStatus {
    /** Saved but not live; may still have diagnostics. */
    DRAFT("draft"),
    /** The live revision for its listener. */
    PUBLISHED("published"),
    /** Was live and can be rolled back to. */
    SUPERSEDED("superseded");

    private final String wire;

    ScriptRevisionStatus(String wire) {
        this.wire = wire;
    }

    /**
     * The value used on the wire.
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
    public static ScriptRevisionStatus fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (ScriptRevisionStatus candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown ScriptRevisionStatus value: " + value);
    }
}
