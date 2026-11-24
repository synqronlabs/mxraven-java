package com.mxraven.mail.notify;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * The audit record of one {@code mx.http} call. Response bodies are never
 * recorded.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class RoutingHTTPCall {
    private final String endpoint;
    private final int status;
    private final long durationMs;
    private final int requestBytes;
    private final int responseBytes;
    private final String error;
    private final boolean cached;

    /** endpoint reference the script named */
    public String endpoint() {
        return endpoint;
    }

    /** HTTP response status, or zero when no response was received */
    public int status() {
        return status;
    }

    /** wall-clock duration in milliseconds */
    public long durationMs() {
        return durationMs;
    }

    /** request body size in bytes */
    public int requestBytes() {
        return requestBytes;
    }

    /** response body size in bytes, capped by the platform */
    public int responseBytes() {
        return responseBytes;
    }

    /** transport failure description, when the call failed */
    public String error() {
        return error;
    }

    public boolean cached() {
        return cached;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoutingHTTPCall that = (RoutingHTTPCall) o;
        return Objects.equals(this.endpoint, that.endpoint) && this.status == that.status && this.durationMs == that.durationMs && this.requestBytes == that.requestBytes && this.responseBytes == that.responseBytes && Objects.equals(this.error, that.error) && this.cached == that.cached;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.endpoint, this.status, this.durationMs, this.requestBytes, this.responseBytes, this.error, this.cached);
    }

    @Override
    public String toString() {
        return "RoutingHTTPCall[" + "endpoint=" + this.endpoint + ", " + "status=" + this.status + ", " + "durationMs=" + this.durationMs + ", " + "requestBytes=" + this.requestBytes + ", " + "responseBytes=" + this.responseBytes + ", " + "error=" + this.error + ", " + "cached=" + this.cached + "]";
    }

    /**
     * Creates a new RoutingHTTPCall.
     *
     * @param endpoint endpoint reference the script named
     * @param status HTTP response status, or zero when no response was received
     * @param durationMs wall-clock duration in milliseconds
     * @param requestBytes request body size in bytes
     * @param responseBytes response body size in bytes, capped by the platform
     * @param error transport failure description, when the call failed
     * @param cached cached
     */
    @JsonCreator
    public RoutingHTTPCall(String endpoint, int status, long durationMs, int requestBytes, int responseBytes, String error, boolean cached) {
        this.endpoint = endpoint;
        this.status = status;
        this.durationMs = durationMs;
        this.requestBytes = requestBytes;
        this.responseBytes = responseBytes;
        this.error = error;
        this.cached = cached;
    }
}
