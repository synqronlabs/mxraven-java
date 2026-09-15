package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** SAML NameID format. */
public enum SamlNameIdFormat {
    UNSPECIFIED("unspecified"),
    PERSISTENT("persistent"),
    TRANSIENT("transient"),
    EMAIL("email");

    private final String wire;

    SamlNameIdFormat(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

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
