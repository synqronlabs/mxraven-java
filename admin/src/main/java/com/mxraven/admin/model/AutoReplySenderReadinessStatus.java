package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Sender-readiness status for an auto-reply template. */
public enum AutoReplySenderReadinessStatus {
    READY("ready"),
    INVALID_FROM_ADDRESS("invalid_from_address"),
    DOMAIN_NOT_FOUND("domain_not_found"),
    SENDING_NOT_ENABLED("sending_not_enabled"),
    DOMAIN_NOT_VERIFIED("domain_not_verified"),
    DKIM_NOT_VERIFIED("dkim_not_verified");

    private final String wire;

    AutoReplySenderReadinessStatus(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

    @JsonCreator
    public static AutoReplySenderReadinessStatus fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (AutoReplySenderReadinessStatus candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown AutoReplySenderReadinessStatus value: " + value);
    }
}
