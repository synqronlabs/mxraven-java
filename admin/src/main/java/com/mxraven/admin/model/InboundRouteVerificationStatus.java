package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** DNS verification status of an inbound route. */
public enum InboundRouteVerificationStatus {
    PENDING("pending"),
    VERIFIED("verified"),
    SUSPENDED("suspended");

    private final String wire;

    InboundRouteVerificationStatus(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

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
