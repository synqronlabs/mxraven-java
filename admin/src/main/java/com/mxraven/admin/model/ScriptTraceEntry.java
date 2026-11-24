package com.mxraven.admin.model;

/**
 * One step in a simulated routing-script trace.
 *
 * @param op the host operation
 * @param detail additional detail, when the host recorded it
 */
public record ScriptTraceEntry(String op, String detail) {
}
