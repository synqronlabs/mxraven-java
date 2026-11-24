package com.mxraven.admin.model;

import java.util.Map;

/**
 * The response an {@code mx.http} call receives during a simulated run. The
 * simulator never performs a real request.
 *
 * @param endpoint endpoint reference the stub answers for
 * @param status HTTP response status
 * @param body response body
 * @param headers response headers
 * @param error transport failure to simulate instead of a response
 */
public record SimulationHTTPStub(
        String endpoint,
        Integer status,
        String body,
        Map<String, String> headers,
        String error) {

    /**
     * Creates a stub, copying the header map.
     */
    public SimulationHTTPStub {
        headers = headers == null ? Map.of() : Map.copyOf(headers);
    }
}
