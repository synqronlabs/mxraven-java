package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Lifecycle state of a connected-application installation. */
public enum ConnectedAppInstallationStatus {
    /** Approved by the application, awaiting a workspace owner's consent. */
    PENDING("pending"),
    /** Approved and usable. */
    ACTIVE("active"),
    /** Access was revoked; the grant is retained as history. */
    REVOKED("revoked");

    private final String wire;

    ConnectedAppInstallationStatus(String wire) {
        this.wire = wire;
    }

    /**
     * The value used on the wire.
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
    public static ConnectedAppInstallationStatus fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (ConnectedAppInstallationStatus candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown ConnectedAppInstallationStatus value: " + value);
    }
}
