package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** SAML NameID format. */
public enum SamlNameIdFormat {
    /** Unspecified NameID format. */
    UNSPECIFIED("unspecified"),
    /** Persistent NameID format. */
    PERSISTENT("persistent"),
    /** Transient NameID format. */
    TRANSIENT("transient"),
    /** Email address NameID format. */
    EMAIL("email");

    private final String wire;

    SamlNameIdFormat(String wire) {
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
    public static SamlNameIdFormat fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (SamlNameIdFormat candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown SamlNameIdFormat value: " + value);
    }
}
