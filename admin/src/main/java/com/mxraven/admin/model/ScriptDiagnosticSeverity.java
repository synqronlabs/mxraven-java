package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Severity of a save-time routing-script diagnostic. */
public enum ScriptDiagnosticSeverity {
    /** The revision cannot be published while this diagnostic is present. */
    ERROR("error"),
    /** The revision can be published, but the diagnostic should be reviewed. */
    WARNING("warning");

    private final String wire;

    ScriptDiagnosticSeverity(String wire) {
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
    public static ScriptDiagnosticSeverity fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (ScriptDiagnosticSeverity candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown ScriptDiagnosticSeverity value: " + value);
    }
}
