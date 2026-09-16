package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Verification status of an SMTP forward destination. */
public enum SmtpForwardDestinationVerificationStatus {
    /** Verification has not completed. */
    PENDING("pending"),
    /** Ownership has been verified. */
    VERIFIED("verified");

    private final String wire;

    SmtpForwardDestinationVerificationStatus(String wire) {
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
