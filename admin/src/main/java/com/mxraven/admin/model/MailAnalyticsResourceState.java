package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** State of a dimension resource in an analytics result. */
public enum MailAnalyticsResourceState {
    /** State does not apply to the dimension. */
    NOT_APPLICABLE("not_applicable"),
    /** Resource still exists. */
    CURRENT("current"),
    /** Resource was deleted or cannot be resolved. */
    DELETED_OR_UNKNOWN("deleted_or_unknown");

    private final String wire;

    MailAnalyticsResourceState(String wire) {
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
     * Resolves the constant matching a wire value.
     *
     * @param value the wire value; may be {@code null}
     * @return the matching constant, or {@code null} when {@code value} is {@code null}
     * @throws IllegalArgumentException if no constant matches
     */
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
