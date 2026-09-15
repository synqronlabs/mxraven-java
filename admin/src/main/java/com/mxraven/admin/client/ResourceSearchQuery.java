package com.mxraven.admin.client;

import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.TenantResourceSearchResult;
import com.mxraven.admin.model.TenantSearchResourceType;

import java.io.IOException;
import java.util.StringJoiner;

/**
 * Typed builder for {@link GovernanceClient#search(String)}.
 */
public final class ResourceSearchQuery {
    private final GovernanceClient client;
    private final QueryParams params = QueryParams.create();

    ResourceSearchQuery(GovernanceClient client) {
        this.client = client;
    }

    /** Required literal search term. */
    public ResourceSearchQuery query(String query) {
        params.q(query);
        return this;
    }

    /** Restrict search to the given resource types (empty means all). */
    public ResourceSearchQuery types(TenantSearchResourceType... types) {
        if (types == null || types.length == 0) {
            return this;
        }
        StringJoiner joiner = new StringJoiner(",");
        for (TenantSearchResourceType type : types) {
            if (type != null) {
                joiner.add(type.wire());
            }
        }
        params.put("types", joiner.toString());
        return this;
    }

    public ResourceSearchQuery pageSize(int pageSize) {
        params.pageSize(pageSize);
        return this;
    }

    public ResourceSearchQuery pageToken(String pageToken) {
        params.pageToken(pageToken);
        return this;
    }

    public Paged<TenantResourceSearchResult> list() throws IOException {
        return client.search(params);
    }
}
