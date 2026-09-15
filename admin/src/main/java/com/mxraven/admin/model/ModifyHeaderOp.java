package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Operation performed by a {@link ModifyHeaderOperation}. */
public enum ModifyHeaderOp {
    APPEND("append"),
    SET("set"),
    REMOVE("remove");

    private final String wire;

    ModifyHeaderOp(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

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
