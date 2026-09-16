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
     * Resolves the disposition for its wire value.
     *
     * @param value the wire value
     * @return the matching disposition, or {@code null} when {@code value} is {@code null}
     * @throws IllegalArgumentException when no disposition matches the value
     */
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
