package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Tenant identity-provisioning state. */
public enum TenantProvisioningState {
    PENDING("pending"),
    PROVISIONING("provisioning"),
    READY("ready"),
    FAILED("failed"),
    DELETING("deleting"),
    DELETED("deleted");

    private final String wire;

    TenantProvisioningState(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

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
