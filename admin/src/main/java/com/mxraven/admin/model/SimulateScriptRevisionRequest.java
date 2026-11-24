package com.mxraven.admin.model;

import java.util.List;
import java.util.Map;

/**
 * Request body for
 * {@code POST /v2/tenants/{slug}/listeners/{id}/script/revisions/{revision}/simulate}.
 *
 * @param fixture synthesized message
 * @param httpStubs responses {@code mx.http} calls receive (at most two)
 * @param recipientSets set name to member addresses, standing in for tenant sets
 */
public record SimulateScriptRevisionRequest(
        SimulationFixture fixture,
        List<SimulationHTTPStub> httpStubs,
        Map<String, List<String>> recipientSets) {

    /**
     * Creates a simulation request, copying every collection.
     */
    public SimulateScriptRevisionRequest {
        httpStubs = httpStubs == null ? List.of() : List.copyOf(httpStubs);
        recipientSets = recipientSets == null ? Map.of() : Map.copyOf(recipientSets);
    }

    /**
     * Creates an empty simulation request: neutral fixture, no stubs or sets.
     *
     * @return the request
     */
    public static SimulateScriptRevisionRequest empty() {
        return new SimulateScriptRevisionRequest(null, null, null);
    }
}
