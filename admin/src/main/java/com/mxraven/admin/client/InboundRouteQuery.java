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

    public InboundRouteQuery search(String query) {
        params.q(query);
        return this;
    }

    public InboundRouteQuery mtaListenerId(String mtaListenerId) {
        params.put("mta_listener_id", mtaListenerId);
        return this;
    }

    public InboundRouteQuery domainName(String domainName) {
        params.put("domain_name", domainName);
        return this;
    }

    public InboundRouteQuery verified(boolean isVerified) {
        params.put("is_verified", isVerified);
        return this;
    }

    public InboundRouteQuery pageSize(int pageSize) {
        params.pageSize(pageSize);
        return this;
    }

    public InboundRouteQuery pageToken(String pageToken) {
        params.pageToken(pageToken);
        return this;
    }

    public Paged<InboundRoute> list() throws IOException {
        return client.list(params);
    }
}
