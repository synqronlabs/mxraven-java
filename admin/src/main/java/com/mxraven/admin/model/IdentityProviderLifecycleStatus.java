package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Lifecycle status of a tenant identity provider. */
public enum IdentityProviderLifecycleStatus {
    /** Provider creation is in progress. */
    PENDING_CREATE("pending_create"),
    /** Provider is provisioned and usable. */
    READY("ready"),
    /** Provider deletion is in progress. */
    PENDING_DELETE("pending_delete"),
    /** Provider creation failed. */
    CREATE_FAILED("create_failed"),
    /** Provider deletion failed. */
    DELETE_FAILED("delete_failed");

    private final String wire;

    IdentityProviderLifecycleStatus(String wire) {
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
     * Resolves the lifecycle status from its wire value.
     *
     * @param value wire value
     * @return matching status, or {@code null} when {@code value} is {@code null}
     * @throws IllegalArgumentException when the value is unknown
     */
    @JsonCreator
    public static IdentityProviderLifecycleStatus fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (IdentityProviderLifecycleStatus candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown IdentityProviderLifecycleStatus value: " + value);
    }
}
