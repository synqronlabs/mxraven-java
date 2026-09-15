package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Outcome recorded for an audit-log entry. */
public enum AuditStatus {
    SUCCESS("success"),
    FAILURE("failure");

    private final String wire;

    AuditStatus(String wire) {
        this.wire = wire;
    }

    /** The {@code snake_case} value used on the wire. */
    @JsonValue
    public String wire() {
        return wire;
    }

    @JsonCreator
    public static AuditStatus fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (AuditStatus candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown AuditStatus value: " + value);
    }
}
