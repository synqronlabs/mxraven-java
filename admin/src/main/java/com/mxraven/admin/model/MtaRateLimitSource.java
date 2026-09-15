package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Effective source of an MTA rate limit. */
public enum MtaRateLimitSource {
    DEFAULT("default"),
    GLOBAL("global"),
    GUARDRAIL("guardrail"),
    TENANT("tenant"),
    LISTENER("listener");

    private final String wire;

    MtaRateLimitSource(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

    @JsonCreator
    public static MtaRateLimitSource fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (MtaRateLimitSource candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown MtaRateLimitSource value: " + value);
    }
}
