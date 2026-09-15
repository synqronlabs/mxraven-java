package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Tenant lifecycle status. */
public enum TenantStatus {
    ACTIVE("active"),
    SUSPENDED("suspended");

    private final String wire;

    TenantStatus(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

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
