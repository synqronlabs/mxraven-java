package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** SAML request binding. */
public enum SamlBinding {
    UNSPECIFIED("unspecified"),
    REDIRECT("redirect"),
    POST("post"),
    ARTIFACT("artifact");

    private final String wire;

    SamlBinding(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

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
