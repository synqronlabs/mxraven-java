package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Terminal action a listener applies when no routing rule matches. */
public enum TerminalActionType {
    /** Deliver through a leased dedicated IP pool. */
    DELIVER_DEDICATED("DELIVER_DEDICATED"),
    /** Deliver using shared delivery. */
    DELIVER("DELIVER"),
    /** Relay through an SMTP smart host. */
    SMARTHOST_RELAY("SMARTHOST_RELAY"),
    /** Relay through an active SMTP relay. */
    RELAY("RELAY"),
    /** Send an automatic reply. */
    AUTO_REPLY("AUTO_REPLY"),
    /** Drop the message. */
    DROP("DROP"),
    /** Reject the message with an SMTP status. */
    REJECT("REJECT");

    private final String wire;

    TerminalActionType(String wire) {
        this.wire = wire;
    }

    /**
     * The {@code snake_case} value used on the wire.
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
    public static TerminalActionType fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (TerminalActionType candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown TerminalActionType value: " + value);
    }
}
