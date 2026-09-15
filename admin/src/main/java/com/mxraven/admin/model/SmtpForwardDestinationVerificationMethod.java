package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Verification method for an SMTP forward destination. */
public enum SmtpForwardDestinationVerificationMethod {
    EMAIL("email"),
    PLATFORM_OVERRIDE("platform_override");

    private final String wire;

    SmtpForwardDestinationVerificationMethod(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

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
