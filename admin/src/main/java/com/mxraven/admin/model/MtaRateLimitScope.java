package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Scope of an MTA rate-limit override. */
public enum MtaRateLimitScope {
    /** Global scope. */
    GLOBAL("global"),
    /** Tenant scope. */
    TENANT("tenant"),
    /** Listener scope. */
    LISTENER("listener");

    private final String wire;

    MtaRateLimitScope(String wire) {
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
    public static MtaRateLimitScope fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (MtaRateLimitScope candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown MtaRateLimitScope value: " + value);
    }
}
