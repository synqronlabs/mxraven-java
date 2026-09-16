package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Kind of actor that produced an audit-log entry. */
public enum AuditActorKind {
    /** A human user. */
    HUMAN("human"),
    /** An automated machine client. */
    MACHINE("machine"),
    /** The platform itself. */
    SYSTEM("system");

    private final String wire;

    AuditActorKind(String wire) {
        this.wire = wire;
    }

    /**
     * The {@code snake_case} value used on the wire.
     *
     * @return the {@code snake_case} value used on the wire
     */
    @JsonValue
    public String wire() {
        return wire;
    }

    /**
     * Resolves the actor kind from its wire value.
     *
     * @param value wire value
     * @return matching kind, or {@code null} when {@code value} is {@code null}
     * @throws IllegalArgumentException when the value is unknown
     */
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
