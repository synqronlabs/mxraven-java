package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Lifecycle status of a tenant domain.
 */
public enum DomainStatus {
    PENDING("pending"),
    VERIFIED("verified"),
    SUSPENDED("suspended");

    private final String wire;

    DomainStatus(String wire) {
        this.wire = wire;
    }

    /** The {@code snake_case} value used on the wire. */
    @JsonValue
    public String wire() {
        return wire;
    }

    @JsonCreator
    public static DomainStatus fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (DomainStatus status : values()) {
            if (status.wire.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("unknown domain status: " + value);
    }
}
