package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Delivery status of an SMTP forward verification email. */
public enum SmtpForwardVerificationDeliveryStatus {
    /** No verification email is available. */
    UNAVAILABLE("unavailable"),
    /** The verification email is queued for delivery. */
    QUEUED("queued"),
    /** The verification email is being sent. */
    SENDING("sending"),
    /** Delivery of the verification email is being retried. */
    RETRY("retry"),
    /** The verification email was sent. */
    SENT("sent"),
    /** Delivery of the verification email failed. */
    FAILED("failed"),
    /** The verification link was consumed. */
    CONSUMED("consumed"),
    /** A newer verification email replaced this one. */
    SUPERSEDED("superseded");

    private final String wire;

    SmtpForwardVerificationDeliveryStatus(String wire) {
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
    public static SmtpForwardVerificationDeliveryStatus fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (SmtpForwardVerificationDeliveryStatus candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown SmtpForwardVerificationDeliveryStatus value: " + value);
    }
}
