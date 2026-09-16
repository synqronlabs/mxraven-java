package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Subdomain scope of a sending-domain grant. */
public enum SendingDomainSubdomainScope {
    /** Applies to the exact domain only. */
    EXACT("exact"),
    /** Applies to the domain and its subdomains. */
    INCLUDE_SUBDOMAINS("include_subdomains");

    private final String wire;

    SendingDomainSubdomainScope(String wire) {
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
    public static SendingDomainSubdomainScope fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (SendingDomainSubdomainScope candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown SendingDomainSubdomainScope value: " + value);
    }
}
