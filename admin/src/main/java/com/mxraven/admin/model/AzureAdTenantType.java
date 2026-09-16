package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Azure AD tenant type. */
public enum AzureAdTenantType {
    /** Work, school, and personal accounts. */
    COMMON("common"),
    /** Work and school accounts only. */
    ORGANISATIONS("organisations"),
    /** Personal Microsoft accounts only. */
    CONSUMERS("consumers");

    private final String wire;

    AzureAdTenantType(String wire) {
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
     * Resolves the tenant type from its wire value.
     *
     * @param value wire value
     * @return matching tenant type, or {@code null} when {@code value} is {@code null}
     * @throws IllegalArgumentException when the value is unknown
     */
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
