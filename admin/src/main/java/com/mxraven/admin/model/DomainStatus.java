package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Lifecycle status of a tenant domain.
 */
public enum DomainStatus {
    /** The domain is awaiting DNS verification. */
    PENDING("pending"),
    /** The domain is verified. */
    VERIFIED("verified"),
    /** The domain is suspended. */
    SUSPENDED("suspended");

    private final String wire;

    DomainStatus(String wire) {
        this.wire = wire;
    }

    /**
     * The {@code snake_case} value used on the wire.
     *
     * @return the {@code snake_case} value used on the wire
     */
    @JsonValue
    public String wire() {
        return wire;
    }

    /**
     * Resolves the domain status from its wire value.
     *
     * @param value wire value
     * @return matching status, or {@code null} when {@code value} is {@code null}
     * @throws IllegalArgumentException when the value is unknown
     */
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
