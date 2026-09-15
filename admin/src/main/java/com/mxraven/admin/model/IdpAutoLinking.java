package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Provider-option auto-linking behaviour. */
public enum IdpAutoLinking {
    UNSPECIFIED("unspecified"),
    USERNAME("username"),
    EMAIL("email");

    private final String wire;

    IdpAutoLinking(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

    @JsonCreator
    public static IdpAutoLinking fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (IdpAutoLinking candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown IdpAutoLinking value: " + value);
    }
}
