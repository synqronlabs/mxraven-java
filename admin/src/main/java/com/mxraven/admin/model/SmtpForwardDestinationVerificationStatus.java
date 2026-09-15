package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Verification status of an SMTP forward destination. */
public enum SmtpForwardDestinationVerificationStatus {
    PENDING("pending"),
    VERIFIED("verified");

    private final String wire;

    SmtpForwardDestinationVerificationStatus(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

    @JsonCreator
    public static SmtpForwardDestinationVerificationStatus fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (SmtpForwardDestinationVerificationStatus candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown SmtpForwardDestinationVerificationStatus value: " + value);
    }
}
