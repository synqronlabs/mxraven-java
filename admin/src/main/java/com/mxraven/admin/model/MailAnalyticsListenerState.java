package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** State of a listener in an analytics breakdown. */
public enum MailAnalyticsListenerState {
    CURRENT("current"),
    DELETED_OR_UNKNOWN("deleted_or_unknown");

    private final String wire;

    MailAnalyticsListenerState(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

    @JsonCreator
    public static MailAnalyticsListenerState fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (MailAnalyticsListenerState candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown MailAnalyticsListenerState value: " + value);
    }
}
