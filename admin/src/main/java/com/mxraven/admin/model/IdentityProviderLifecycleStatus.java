package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Lifecycle status of a tenant identity provider. */
public enum IdentityProviderLifecycleStatus {
    PENDING_CREATE("pending_create"),
    READY("ready"),
    PENDING_DELETE("pending_delete"),
    CREATE_FAILED("create_failed"),
    DELETE_FAILED("delete_failed");

    private final String wire;

    IdentityProviderLifecycleStatus(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

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
