package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Effective source of an MTA rate limit. */
public enum MtaRateLimitSource {
    /** Built-in default limit. */
    DEFAULT("default"),
    /** Global override. */
    GLOBAL("global"),
    /** Guardrail limit. */
    GUARDRAIL("guardrail"),
    /** Tenant override. */
    TENANT("tenant"),
    /** Listener override. */
    LISTENER("listener");

    private final String wire;

    MtaRateLimitSource(String wire) {
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
