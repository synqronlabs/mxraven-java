package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** How an inbound recipient routing outcome was disposed of. */
public enum InboundRoutingOutcomeDisposition {
    /** The whole transaction was rejected over SMTP. */
    SMTP_REJECTED("smtp_rejected"),
    /** One recipient was suppressed while the rest of the transaction continued. */
    MIXED_SUPPRESSED("mixed_suppressed");

    private final String wire;

    InboundRoutingOutcomeDisposition(String wire) {
        this.wire = wire;
    }

    /**
     * The value used on the wire.
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
    public static InboundRoutingOutcomeDisposition fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (InboundRoutingOutcomeDisposition candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown InboundRoutingOutcomeDisposition value: " + value);
    }
}
