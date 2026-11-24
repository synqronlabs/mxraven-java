package com.mxraven.admin.model;

/**
 * One save-time routing-script finding with an editor location.
 *
 * @param severity whether the finding blocks publication
 * @param line one-based source line, or zero when unknown
 * @param column one-based source column, or zero when only the line is known
 * @param message human-readable description
 * @param hint suggested fix, when the compiler has one
 */
public record ScriptDiagnostic(
        ScriptDiagnosticSeverity severity,
        int line,
        int column,
        String message,
        String hint) {
}
