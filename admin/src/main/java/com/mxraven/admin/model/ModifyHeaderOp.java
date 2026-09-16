package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Operation performed by a {@link ModifyHeaderOperation}. */
public enum ModifyHeaderOp {
    /** Appends the value to the header. */
    APPEND("append"),
    /** Replaces the header value. */
    SET("set"),
    /** Removes the header. */
    REMOVE("remove");

    private final String wire;

    ModifyHeaderOp(String wire) {
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
    public static ModifyHeaderOp fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (ModifyHeaderOp candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown ModifyHeaderOp value: " + value);
    }
}
