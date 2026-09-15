package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Kind of actor that produced an audit-log entry. */
public enum AuditActorKind {
    HUMAN("human"),
    MACHINE("machine"),
    SYSTEM("system");

    private final String wire;

    AuditActorKind(String wire) {
        this.wire = wire;
    }

    /** The {@code snake_case} value used on the wire. */
    @JsonValue
    public String wire() {
        return wire;
    }

    @JsonCreator
    public static AuditActorKind fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (AuditActorKind candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown AuditActorKind value: " + value);
    }
}
