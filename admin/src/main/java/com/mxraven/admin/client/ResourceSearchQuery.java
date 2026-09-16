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

    /**
     * Sets the required literal search term.
     *
     * @param query literal search term
     * @return this query
     * @throws IllegalArgumentException if the query is non-blank and shorter than
     *                                  {@value QueryParams#MIN_SEARCH_LENGTH} characters
     */
    public ResourceSearchQuery query(String query) {
        params.q(query);
        return this;
    }

    /**
     * Restricts search to the given resource types (empty means all).
     *
     * @param types resource types to include; {@code null} or empty means all
     * @return this query
     */
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

    /**
     * Sets the maximum number of results per page.
     *
     * @param pageSize number of items per page, between 1 and 500
     * @return this query
     * @throws IllegalArgumentException if {@code pageSize} is outside the range 1 to 500
     */
    public ResourceSearchQuery pageSize(int pageSize) {
        params.pageSize(pageSize);
        return this;
    }

    /**
     * Sets the opaque page token to continue from.
     *
     * @param pageToken opaque page token; {@code null} is ignored
     * @return this query
     */
    public ResourceSearchQuery pageToken(String pageToken) {
        params.pageToken(pageToken);
        return this;
    }

    /**
     * Executes the search.
     *
     * @return a lazily paginated collection of search results
     * @throws IOException if the request fails or is interrupted
     */
    public Paged<TenantResourceSearchResult> list() throws IOException {
        return client.search(params);
    }
}
