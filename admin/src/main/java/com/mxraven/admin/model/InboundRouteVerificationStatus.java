package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** DNS verification status of an inbound route. */
public enum InboundRouteVerificationStatus {
    /** DNS verification is pending. */
    PENDING("pending"),
    /** DNS ownership is verified. */
    VERIFIED("verified"),
    /** The route has been suspended. */
    SUSPENDED("suspended");

    private final String wire;

    InboundRouteVerificationStatus(String wire) {
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
     * Resolves the constant matching a wire value.
     *
     * @param value the wire value; may be {@code null}
     * @return the matching constant, or {@code null} when {@code value} is {@code null}
     * @throws IllegalArgumentException if no constant matches
     */
    @JsonCreator
    public static InboundRouteVerificationStatus fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (InboundRouteVerificationStatus candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown InboundRouteVerificationStatus value: " + value);
    }
}
