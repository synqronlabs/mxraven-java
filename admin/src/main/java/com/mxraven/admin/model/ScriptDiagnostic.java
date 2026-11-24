package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * One save-time routing-script finding with an editor location.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ScriptDiagnostic {
    private final ScriptDiagnosticSeverity severity;
    private final int line;
    private final int column;
    private final String message;
    private final String hint;

    /** whether the finding blocks publication */
    public ScriptDiagnosticSeverity severity() {
        return severity;
    }

    /** one-based source line, or zero when unknown */
    public int line() {
        return line;
    }

    /** one-based source column, or zero when only the line is known */
    public int column() {
        return column;
    }

    /** human-readable description */
    public String message() {
        return message;
    }

    public String hint() {
        return hint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScriptDiagnostic that = (ScriptDiagnostic) o;
        return Objects.equals(this.severity, that.severity) && this.line == that.line && this.column == that.column && Objects.equals(this.message, that.message) && Objects.equals(this.hint, that.hint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.severity, this.line, this.column, this.message, this.hint);
    }

    @Override
    public String toString() {
        return "ScriptDiagnostic[" + "severity=" + this.severity + ", " + "line=" + this.line + ", " + "column=" + this.column + ", " + "message=" + this.message + ", " + "hint=" + this.hint + "]";
    }

    /**
     * Creates a new ScriptDiagnostic.
     *
     * @param severity whether the finding blocks publication
     * @param line one-based source line, or zero when unknown
     * @param column one-based source column, or zero when only the line is known
     * @param message human-readable description
     * @param hint hint
     */
    @JsonCreator
    public ScriptDiagnostic(ScriptDiagnosticSeverity severity, int line, int column, String message, String hint) {
        this.severity = severity;
        this.line = line;
        this.column = column;
        this.message = message;
        this.hint = hint;
    }
}
