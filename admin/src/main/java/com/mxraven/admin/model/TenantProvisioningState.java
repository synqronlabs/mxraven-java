package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Tenant identity-provisioning state. */
public enum TenantProvisioningState {
    /** Provisioning has not started. */
    PENDING("pending"),
    /** Provisioning is in progress. */
    PROVISIONING("provisioning"),
    /** Provisioning completed successfully. */
    READY("ready"),
    /** Provisioning failed. */
    FAILED("failed"),
    /** The tenant is being deleted. */
    DELETING("deleting"),
    /** The tenant is deleted. */
    DELETED("deleted");

    private final String wire;

    TenantProvisioningState(String wire) {
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
    public static TenantProvisioningState fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (TenantProvisioningState candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown TenantProvisioningState value: " + value);
    }
}
