package com.mxraven.admin.client;

import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.InboundRoute;

import java.io.IOException;

/**
 * Typed filter builder for {@link InboundRoutesClient#query()}.
 */
public final class InboundRouteQuery {
    private final InboundRoutesClient client;
    private final QueryParams params = QueryParams.create();

    InboundRouteQuery(InboundRoutesClient client) {
        this.client = client;
    }

    /**
     * Set the {@code q} search filter.
     *
     * @param query search text
     * @return this query
     */
    public InboundRouteQuery search(String query) {
        params.q(query);
        return this;
    }

    /**
     * Filter by MTA listener identifier.
     *
     * @param mtaListenerId MTA listener identifier
     * @return this query
     */
    public InboundRouteQuery mtaListenerId(String mtaListenerId) {
        params.put("mta_listener_id", mtaListenerId);
        return this;
    }

    /**
     * Filter by domain name.
     *
     * @param domainName domain name
     * @return this query
     */
    public InboundRouteQuery domainName(String domainName) {
        params.put("domain_name", domainName);
        return this;
    }

    /**
     * Filter by verification state.
     *
     * @param isVerified required verification state
     * @return this query
     */
    public InboundRouteQuery verified(boolean isVerified) {
        params.put("is_verified", isVerified);
        return this;
    }

    /**
     * Set the maximum number of inbound routes per page.
     *
     * @param pageSize page size, between 1 and 500
     * @return this query
     */
    public InboundRouteQuery pageSize(int pageSize) {
        params.pageSize(pageSize);
        return this;
    }

    /**
     * Continue from a previously returned page.
     *
     * @param pageToken opaque page token; {@code null} is ignored
     * @return this query
     */
    public InboundRouteQuery pageToken(String pageToken) {
        params.pageToken(pageToken);
        return this;
    }

    /**
     * Fetch the matching inbound routes (first page fetched eagerly, remaining
     * pages lazy).
     *
     * @return a lazily paginating collection of inbound routes
     * @throws IOException if the first page request fails or is interrupted
     */
    public Paged<InboundRoute> list() throws IOException {
        return client.list(params);
    }
}
