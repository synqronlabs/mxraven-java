package com.mxraven.admin.client;

import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.ScriptRevisionSummary;

import java.io.IOException;

/**
 * Typed page builder for {@link RoutingScriptsClient#query()}.
 */
public final class ScriptRevisionQuery {
    private final RoutingScriptsClient client;
    private final QueryParams params = QueryParams.create();

    ScriptRevisionQuery(RoutingScriptsClient client) {
        this.client = client;
    }

    /**
     * Set the maximum number of revisions per page.
     *
     * @param pageSize page size, between 1 and 500
     * @return this query
     */
    public ScriptRevisionQuery pageSize(int pageSize) {
        params.pageSize(pageSize);
        return this;
    }

    /**
     * Continue from a previously returned page.
     *
     * @param pageToken opaque page token; {@code null} is ignored
     * @return this query
     */
    public ScriptRevisionQuery pageToken(String pageToken) {
        params.pageToken(pageToken);
        return this;
    }

    /**
     * Fetch the matching revisions, newest first.
     *
     * @return a lazily paginating collection of revision summaries
     * @throws IOException if the first page request fails or is interrupted
     */
    public Paged<ScriptRevisionSummary> list() throws IOException {
        return client.listRevisions(params);
    }
}
