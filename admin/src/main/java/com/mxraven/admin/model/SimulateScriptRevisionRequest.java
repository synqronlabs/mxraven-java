package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;
import java.util.List;
import java.util.Map;

/**
 * Request body for
 * {@code POST /v2/tenants/{slug}/listeners/{id}/script/revisions/{revision}/simulate}.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class SimulateScriptRevisionRequest {
    private final SimulationFixture fixture;
    private final List<SimulationHTTPStub> httpStubs;
    private final Map<String, List<String>> recipientSets;

    /** synthesized message */
    public SimulationFixture fixture() {
        return fixture;
    }

    /** responses {@code mx.http} calls receive (at most two) */
    public List<SimulationHTTPStub> httpStubs() {
        return httpStubs;
    }

    public Map<String, List<String>> recipientSets() {
        return recipientSets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimulateScriptRevisionRequest that = (SimulateScriptRevisionRequest) o;
        return Objects.equals(this.fixture, that.fixture) && Objects.equals(this.httpStubs, that.httpStubs) && Objects.equals(this.recipientSets, that.recipientSets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.fixture, this.httpStubs, this.recipientSets);
    }

    @Override
    public String toString() {
        return "SimulateScriptRevisionRequest[" + "fixture=" + this.fixture + ", " + "httpStubs=" + this.httpStubs + ", " + "recipientSets=" + this.recipientSets + "]";
    }

    /**
     * Creates a new SimulateScriptRevisionRequest.
     *
     * @param fixture synthesized message
     * @param httpStubs responses {@code mx.http} calls receive (at most two)
     * @param recipientSets recipientSets
     */
    @JsonCreator
    public SimulateScriptRevisionRequest(SimulationFixture fixture, List<SimulationHTTPStub> httpStubs, Map<String, List<String>> recipientSets) {
        this.fixture = fixture;
        this.httpStubs = httpStubs == null ? Java8.list() : Java8.copyList(httpStubs);
        this.recipientSets = recipientSets == null ? Java8.map() : Java8.copyMap(recipientSets);
    }

    /**
         * Creates a simulation request, copying every collection.
         */


        /**
         * Creates an empty simulation request: neutral fixture, no stubs or sets.
         *
         * @return the request
         */
        public static SimulateScriptRevisionRequest empty() {
            return new SimulateScriptRevisionRequest(null, null, null);
        }
}
