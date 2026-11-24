package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Why a routing script faulted and the listener-type default applied.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ScriptFault {
    private final ScriptFaultReason reason;
    private final String message;
    private final Integer line;

    /** stable fault reason */
    public ScriptFaultReason reason() {
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
        ScriptFault that = (ScriptFault) o;
        return Objects.equals(this.reason, that.reason) && Objects.equals(this.message, that.message) && Objects.equals(this.line, that.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.reason, this.message, this.line);
    }

    @Override
    public String toString() {
        return "ScriptFault[" + "reason=" + this.reason + ", " + "message=" + this.message + ", " + "line=" + this.line + "]";
    }

    /**
     * Creates a new ScriptFault.
     *
     * @param reason stable fault reason
     * @param message human-readable failure description
     * @param line line
     */
    @JsonCreator
    public ScriptFault(ScriptFaultReason reason, String message, Integer line) {
        this.reason = reason;
        this.message = message;
        this.line = line;
    }
}
