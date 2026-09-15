package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Subdomain scope of a sending-domain grant. */
public enum SendingDomainSubdomainScope {
    EXACT("exact"),
    INCLUDE_SUBDOMAINS("include_subdomains");

    private final String wire;

    SendingDomainSubdomainScope(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

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
