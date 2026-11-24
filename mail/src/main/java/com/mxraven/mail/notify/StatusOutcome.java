package com.mxraven.mail.notify;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** The lifecycle state reported by a delivery-attempt event. */
public enum StatusOutcome {
    /** A delivery attempt started. */
    ATTEMPTED("attempted"),
    /** The destination accepted the message. */
    DELIVERED("delivered"),
    /** The destination temporarily rejected the message. */
    DEFERRED("deferred"),
    /** Delivery failed permanently. */
    FAILED("failed"),
    /** The delivery deadline passed before success. */
    EXPIRED("expired"),
    /** Delivery was skipped by a suppression rule (SMTP deliveries only). */
    SUPPRESSED("suppressed");

    private final String wire;

    StatusOutcome(String wire) {
        this.wire = wire;
    }

    /**
     * Returns the value used on the wire.
     *
     * @return the wire value
     */
    @JsonValue
    public String wire() {
        return wire;
    }

    /**
     * Resolves the outcome for its wire value.
     *
     * @param value the wire value
     * @return the matching outcome, or {@code null} when {@code value} is {@code null}
     * @throws IllegalArgumentException when no outcome matches the value
     */
    @JsonCreator
    public static StatusOutcome fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (StatusOutcome candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown status outcome: " + value);
    }
}
