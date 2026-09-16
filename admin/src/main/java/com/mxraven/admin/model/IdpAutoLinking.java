package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Provider-option auto-linking behaviour. */
public enum IdpAutoLinking {
    /** Auto-linking behaviour is unspecified. */
    UNSPECIFIED("unspecified"),
    /** Links identities by username. */
    USERNAME("username"),
    /** Links identities by email address. */
    EMAIL("email");

    private final String wire;

    IdpAutoLinking(String wire) {
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
     * Resolves the constant matching a wire value.
     *
     * @param value the wire value; may be {@code null}
     * @return the matching constant, or {@code null} when {@code value} is {@code null}
     * @throws IllegalArgumentException if no constant matches
     */
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
