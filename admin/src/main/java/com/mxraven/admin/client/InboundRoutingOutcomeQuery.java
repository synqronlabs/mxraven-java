package com.mxraven.admin.client;

import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.InboundRoutingOutcome;

import java.io.IOException;

/**
 * Typed filter builder for
 * {@link GovernanceClient#inboundRoutingOutcomeQuery()}.
 */
public final class InboundRoutingOutcomeQuery {
    private final GovernanceClient client;
    private final QueryParams params = QueryParams.create();

    InboundRoutingOutcomeQuery(GovernanceClient client) {
        this.client = client;
    }

    /**
     * Filter by listener identifier.
     *
     * @param listenerId listener identifier; {@code null} is ignored
     * @return this query
     */
    public InboundRoutingOutcomeQuery listenerId(String listenerId) {
        params.put("listener_id", listenerId);
        return this;
    }

    /**
     * Set the maximum number of outcomes per page.
     *
     * @param pageSize page size, between 1 and 500
     * @return this query
     */
    public InboundRoutingOutcomeQuery pageSize(int pageSize) {
        params.pageSize(pageSize);
        return this;
    }

    /**
     * Continue from a previously returned page.
     *
     * @param pageToken opaque page token; {@code null} is ignored
     * @return this query
     */
    public InboundRoutingOutcomeQuery pageToken(String pageToken) {
        params.pageToken(pageToken);
        return this;
    }

    /**
     * Fetch the matching outcomes, newest first.
     *
     * @return a lazily paginating collection of inbound routing outcomes
     * @throws IOException if the first page request fails or is interrupted
     */
    public Paged<InboundRoutingOutcome> list() throws IOException {
        return client.listInboundRoutingOutcomes(params);
    }
}
