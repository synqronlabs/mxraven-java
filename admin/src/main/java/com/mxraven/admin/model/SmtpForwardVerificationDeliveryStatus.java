package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Delivery status of an SMTP forward verification email. */
public enum SmtpForwardVerificationDeliveryStatus {
    UNAVAILABLE("unavailable"),
    QUEUED("queued"),
    SENDING("sending"),
    RETRY("retry"),
    SENT("sent"),
    FAILED("failed"),
    CONSUMED("consumed"),
    SUPERSEDED("superseded");

    private final String wire;

    SmtpForwardVerificationDeliveryStatus(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

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
