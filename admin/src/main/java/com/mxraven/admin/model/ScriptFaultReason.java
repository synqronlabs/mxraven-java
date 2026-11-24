package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Stable reason a routing script faulted and the listener default applied. */
public enum ScriptFaultReason {
    /** The script raised an error or exceeded its instruction budget. */
    RUNTIME_ERROR("runtime_error"),
    /** The invocation exceeded its wall-clock deadline. */
    DEADLINE_EXCEEDED("deadline_exceeded"),
    /** The invocation exceeded its memory budget. */
    MEMORY_EXHAUSTED("memory_exhausted"),
    /** The script staged an effect the validator rejected at runtime. */
    INVALID_EFFECT("invalid_effect"),
    /** The listener exhausted its routing budget. */
    ROUTING_BUDGET_EXHAUSTED("routing_budget_exhausted"),
    /** The published revision was quarantined after repeated faults. */
    QUARANTINED("quarantined");

    private final String wire;

    ScriptFaultReason(String wire) {
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
    public static ScriptFaultReason fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (ScriptFaultReason candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown ScriptFaultReason value: " + value);
    }
}
