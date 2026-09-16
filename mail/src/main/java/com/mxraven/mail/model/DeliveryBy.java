package com.mxraven.mail.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * The DELIVERBY parameter of RFC 2852: a delivery time limit, a notification
 * mode, and whether a trace is requested.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class DeliveryBy {
    private final long seconds;
    private final DeliveryByMode mode;
    private final boolean trace;

    /** the time limit in seconds */
    public long seconds() {
        return seconds;
    }

    /** the requested notification mode */
    public DeliveryByMode mode() {
        return mode;
    }

    /** whether a trace of the delivery path is requested */
    public boolean trace() {
        return trace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeliveryBy that = (DeliveryBy) o;
        return this.seconds == that.seconds
                && Objects.equals(this.mode, that.mode)
                && this.trace == that.trace;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.seconds, this.mode, this.trace);
    }

    @Override
    public String toString() {
        return "DeliveryBy[" + "seconds=" + this.seconds + ", " + "mode=" + this.mode + ", " + "trace=" + this.trace + "]";
    }

    /**
     * Creates a new DeliveryBy.
     *
     * @param seconds the time limit in seconds
     * @param mode the requested notification mode
     * @param trace whether a trace of the delivery path is requested
     */
    @JsonCreator
    public DeliveryBy(long seconds, DeliveryByMode mode, boolean trace) {
        this.seconds = seconds;
        this.mode = mode;
        this.trace = trace;
    }
}
