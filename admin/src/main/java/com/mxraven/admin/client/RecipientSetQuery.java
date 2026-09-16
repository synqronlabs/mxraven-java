package com.mxraven.admin.client;

import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.RecipientSet;

import java.io.IOException;

/**
 * Typed filter builder for {@link RecipientSetsClient#query()}.
 */
public final class RecipientSetQuery {
    private final RecipientSetsClient client;
    private final QueryParams params = QueryParams.create();

    RecipientSetQuery(RecipientSetsClient client) {
        this.client = client;
    }

    /**
     * Sets the {@code q} search filter for recipient sets.
     *
     * @param query search text; {@code null} or blank is ignored
     * @return this query
     * @throws IllegalArgumentException if the query is non-blank and shorter than
     *                                  {@value QueryParams#MIN_SEARCH_LENGTH} characters
     */
    public RecipientSetQuery search(String query) {
        params.q(query);
        return this;
    }

    /**
     * Sets the maximum number of recipient sets per page.
     *
     * @param pageSize number of items per page, between 1 and 500
     * @return this query
     * @throws IllegalArgumentException if {@code pageSize} is outside the range 1 to 500
     */
    public RecipientSetQuery pageSize(int pageSize) {
        params.pageSize(pageSize);
        return this;
    }

    /**
     * Sets the opaque page token to continue from.
     *
     * @param pageToken opaque page token; {@code null} is ignored
     * @return this query
     */
    public RecipientSetQuery pageToken(String pageToken) {
        params.pageToken(pageToken);
        return this;
    }

    /**
     * Lists recipient sets matching the configured filters.
     *
     * @return a lazily paginated collection of recipient sets
     * @throws IOException if the request fails or is interrupted
     */
    public Paged<RecipientSet> list() throws IOException {
        return client.list(params);
    }
}
