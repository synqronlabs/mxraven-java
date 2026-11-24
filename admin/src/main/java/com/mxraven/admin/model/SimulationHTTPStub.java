package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;
import java.util.Map;

/**
 * The response an {@code mx.http} call receives during a simulated run. The
 * simulator never performs a real request.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class SimulationHTTPStub {
    private final String endpoint;
    private final Integer status;
    private final String body;
    private final Map<String, String> headers;
    private final String error;

    /** endpoint reference the stub answers for */
    public String endpoint() {
        return endpoint;
    }

    /** HTTP response status */
    public Integer status() {
        return status;
    }

    /** response body */
    public String body() {
        return body;
    }

    /** response headers */
    public Map<String, String> headers() {
        return headers;
    }

    public String error() {
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimulationHTTPStub that = (SimulationHTTPStub) o;
        return Objects.equals(this.endpoint, that.endpoint) && Objects.equals(this.status, that.status) && Objects.equals(this.body, that.body) && Objects.equals(this.headers, that.headers) && Objects.equals(this.error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.endpoint, this.status, this.body, this.headers, this.error);
    }

    @Override
    public String toString() {
        return "SimulationHTTPStub[" + "endpoint=" + this.endpoint + ", " + "status=" + this.status + ", " + "body=" + this.body + ", " + "headers=" + this.headers + ", " + "error=" + this.error + "]";
    }

    /**
     * Creates a new SimulationHTTPStub.
     *
     * @param endpoint endpoint reference the stub answers for
     * @param status HTTP response status
     * @param body response body
     * @param headers response headers
     * @param error error
     */
    @JsonCreator
    public SimulationHTTPStub(String endpoint, Integer status, String body, Map<String, String> headers, String error) {
        this.endpoint = endpoint;
        this.status = status;
        this.body = body;
        this.error = error;
        this.headers = headers == null ? Java8.map() : Java8.copyMap(headers);
    }

    /**
         * Creates a stub, copying the header map.
         */
}
