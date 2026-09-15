package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Scope of an MTA rate-limit override. */
public enum MtaRateLimitScope {
    GLOBAL("global"),
    TENANT("tenant"),
    LISTENER("listener");

    private final String wire;

    MtaRateLimitScope(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

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
