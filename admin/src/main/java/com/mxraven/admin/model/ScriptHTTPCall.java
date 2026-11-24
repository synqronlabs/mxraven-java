package com.mxraven.admin.model;

/**
 * The audit record of one {@code mx.http} call. Response bodies are never kept.
 *
 * @param endpoint endpoint reference the script named
 * @param status HTTP response status, or zero when no response was received
 * @param durationMs wall-clock duration in milliseconds
 * @param requestBytes request body size in bytes
 * @param responseBytes response body size in bytes, capped by the platform
 * @param error transport failure description, when the call failed
 */
public record ScriptHTTPCall(
        String endpoint,
        int status,
        long durationMs,
        int requestBytes,
        int responseBytes,
        String error) {
}
