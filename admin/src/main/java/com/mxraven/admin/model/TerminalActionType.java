package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Terminal action a listener applies when no routing rule matches. */
public enum TerminalActionType {
    DELIVER_DEDICATED("DELIVER_DEDICATED"),
    DELIVER("DELIVER"),
    SMARTHOST_RELAY("SMARTHOST_RELAY"),
    RELAY("RELAY"),
    AUTO_REPLY("AUTO_REPLY"),
    DROP("DROP"),
    REJECT("REJECT");

    private final String wire;

    TerminalActionType(String wire) {
        this.wire = wire;
    }

    /** The {@code snake_case} value used on the wire. */
    @JsonValue
    public String wire() {
        return wire;
    }

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
