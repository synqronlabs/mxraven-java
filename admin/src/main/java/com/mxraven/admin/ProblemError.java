package com.mxraven.admin;

/**
 * One actionable validation issue from an RFC 9457 problem response.
 *
 * @param pointer RFC 6901 JSON Pointer to the invalid request value, when applicable
 * @param code    stable machine-readable validation issue code
 * @param detail  safe human-readable explanation
 */
public record ProblemError(String pointer, String code, String detail) {
}
