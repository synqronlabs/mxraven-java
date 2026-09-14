package com.mxraven.mail.feedback;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The training label applied to a feedback submission.
 */
public enum Disposition {
    /** Marks the message as spam. */
    SPAM("spam"),
    /** Marks the message as not spam. */
    HAM("ham");

    private final String wire;

    Disposition(String wire) {
        this.wire = wire;
    }

    /** The value used on the wire. */
    @JsonValue
    public String wire() {
        return wire;
    }

    @JsonCreator
    public static Disposition fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (Disposition candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown disposition: " + value);
    }
}
