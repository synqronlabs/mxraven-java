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

    public RecipientSetQuery search(String query) {
        params.q(query);
        return this;
    }

    public RecipientSetQuery pageSize(int pageSize) {
        params.pageSize(pageSize);
        return this;
    }

    public RecipientSetQuery pageToken(String pageToken) {
        params.pageToken(pageToken);
        return this;
    }

    public Paged<RecipientSet> list() throws IOException {
        return client.list(params);
    }
}
