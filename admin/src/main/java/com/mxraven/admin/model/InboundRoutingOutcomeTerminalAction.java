package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Terminal action recorded for an inbound routing outcome. */
public enum InboundRoutingOutcomeTerminalAction {
    /** The transaction was rejected. */
    REJECT("REJECT");

    private final String wire;

    InboundRoutingOutcomeTerminalAction(String wire) {
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
    public static InboundRoutingOutcomeTerminalAction fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (InboundRoutingOutcomeTerminalAction candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown InboundRoutingOutcomeTerminalAction value: " + value);
    }
}
