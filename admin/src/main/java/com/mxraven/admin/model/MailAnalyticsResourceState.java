package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** State of a dimension resource in an analytics result. */
public enum MailAnalyticsResourceState {
    NOT_APPLICABLE("not_applicable"),
    CURRENT("current"),
    DELETED_OR_UNKNOWN("deleted_or_unknown");

    private final String wire;

    MailAnalyticsResourceState(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

    @JsonCreator
    public static MailAnalyticsResourceState fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (MailAnalyticsResourceState candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown MailAnalyticsResourceState value: " + value);
    }
}
