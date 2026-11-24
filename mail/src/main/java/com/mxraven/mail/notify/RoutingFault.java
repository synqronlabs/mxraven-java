package com.mxraven.mail.notify;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Why a routing script faulted and the listener-type default applied.
 *
 * <p>{@link #message()} may contain tenant-authored text. Do not treat it as
 * trusted markup or interpolate it into a log line unchecked.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class RoutingFault {
    private final String reason;
    private final String message;
    private final Integer line;

    /** stable fault reason, for example {@code quarantined} */
    public String reason() {
        return reason;
    }

    /** human-readable failure description */
    public String message() {
        return message;
    }

    public Integer line() {
        return line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoutingFault that = (RoutingFault) o;
        return Objects.equals(this.reason, that.reason) && Objects.equals(this.message, that.message) && Objects.equals(this.line, that.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.reason, this.message, this.line);
    }

    @Override
    public String toString() {
        return "RoutingFault[" + "reason=" + this.reason + ", " + "message=" + this.message + ", " + "line=" + this.line + "]";
    }

    /**
     * Creates a new RoutingFault.
     *
     * @param reason stable fault reason, for example {@code quarantined}
     * @param message human-readable failure description
     * @param line line
     */
    @JsonCreator
    public RoutingFault(String reason, String message, Integer line) {
        this.reason = reason;
        this.message = message;
        this.line = line;
    }
}
