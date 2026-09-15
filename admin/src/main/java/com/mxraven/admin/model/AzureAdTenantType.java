package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Azure AD tenant type. */
public enum AzureAdTenantType {
    COMMON("common"),
    ORGANISATIONS("organisations"),
    CONSUMERS("consumers");

    private final String wire;

    AzureAdTenantType(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

    @JsonCreator
    public static AzureAdTenantType fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (AzureAdTenantType candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown AzureAdTenantType value: " + value);
    }
}
