package com.mxraven.mail.notify;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Identifies the egress worker that produced an attempt event. */
public enum AttemptChannel {
    /** An SMTP delivery attempt. */
    SMTP("smtp"),
    /** An object-storage write attempt. */
    S3("s3"),
    /** A webhook delivery attempt. */
    WEBHOOK("webhook");

    private final String wire;

    AttemptChannel(String wire) {
        this.wire = wire;
    }

    /**
     * Returns the value used on the wire.
     *
     * @return the wire value
     */
    @JsonValue
    public String wire() {
        return wire;
    }

    /**
     * Resolves the channel for its wire value.
     *
     * @param value the wire value
     * @return the matching channel, or {@code null} when {@code value} is {@code null}
     * @throws IllegalArgumentException when no channel matches the value
     */
    @JsonCreator
    public static AttemptChannel fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (AttemptChannel candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown attempt channel: " + value);
    }
}
