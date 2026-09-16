package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Verification method for an SMTP forward destination. */
public enum SmtpForwardDestinationVerificationMethod {
    /** Ownership is verified by email. */
    EMAIL("email"),
    /** Ownership is verified by a platform override. */
    PLATFORM_OVERRIDE("platform_override");

    private final String wire;

    SmtpForwardDestinationVerificationMethod(String wire) {
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
    public static SmtpForwardDestinationVerificationMethod fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (SmtpForwardDestinationVerificationMethod candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown SmtpForwardDestinationVerificationMethod value: " + value);
    }
}
