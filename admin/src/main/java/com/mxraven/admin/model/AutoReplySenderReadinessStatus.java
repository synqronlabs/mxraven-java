package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Sender-readiness status for an auto-reply template. */
public enum AutoReplySenderReadinessStatus {
    /** The sender is ready to deliver. */
    READY("ready"),
    /** The configured from address is not a valid mailbox. */
    INVALID_FROM_ADDRESS("invalid_from_address"),
    /** The sender domain is not onboarded. */
    DOMAIN_NOT_FOUND("domain_not_found"),
    /** Sending is not enabled for the domain. */
    SENDING_NOT_ENABLED("sending_not_enabled"),
    /** The domain is not verified. */
    DOMAIN_NOT_VERIFIED("domain_not_verified"),
    /** DKIM is not verified for the domain. */
    DKIM_NOT_VERIFIED("dkim_not_verified");

    private final String wire;

    AutoReplySenderReadinessStatus(String wire) {
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
     * Resolves the readiness status from its wire value.
     *
     * @param value wire value
     * @return matching status, or {@code null} when {@code value} is {@code null}
     * @throws IllegalArgumentException when the value is unknown
     */
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
