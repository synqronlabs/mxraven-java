package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Tenant lifecycle status. */
public enum TenantStatus {
    /** The tenant is active. */
    ACTIVE("active"),
    /** The tenant is suspended. */
    SUSPENDED("suspended");

    private final String wire;

    TenantStatus(String wire) {
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
    public static TenantStatus fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (TenantStatus candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown TenantStatus value: " + value);
    }
}
