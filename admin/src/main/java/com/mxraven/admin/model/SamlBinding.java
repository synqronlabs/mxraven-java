package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** SAML request binding. */
public enum SamlBinding {
    /** Unspecified binding. */
    UNSPECIFIED("unspecified"),
    /** HTTP redirect binding. */
    REDIRECT("redirect"),
    /** HTTP POST binding. */
    POST("post"),
    /** SAML artifact binding. */
    ARTIFACT("artifact");

    private final String wire;

    SamlBinding(String wire) {
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
    public static SamlBinding fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (SamlBinding candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown SamlBinding value: " + value);
    }
}
