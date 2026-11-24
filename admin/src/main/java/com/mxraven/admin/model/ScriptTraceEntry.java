package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * One step in a simulated routing-script trace.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ScriptTraceEntry {
    private final String op;
    private final String detail;

    /** the host operation */
    public String op() {
        return op;
    }

    public String detail() {
        return detail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScriptTraceEntry that = (ScriptTraceEntry) o;
        return Objects.equals(this.op, that.op) && Objects.equals(this.detail, that.detail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.op, this.detail);
    }

    @Override
    public String toString() {
        return "ScriptTraceEntry[" + "op=" + this.op + ", " + "detail=" + this.detail + "]";
    }

    /**
     * Creates a new ScriptTraceEntry.
     *
     * @param op the host operation
     * @param detail detail
     */
    @JsonCreator
    public ScriptTraceEntry(String op, String detail) {
        this.op = op;
        this.detail = detail;
    }
}
