package com.mxraven.admin.client;

import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.SuppressionReason;
import com.mxraven.admin.model.TenantSuppression;

import java.io.IOException;

/**
 * Typed filter builder for {@link SuppressionsClient#query()}.
 */
public final class SuppressionQuery {
    private final SuppressionsClient client;
    private final QueryParams params = QueryParams.create();

    SuppressionQuery(SuppressionsClient client) {
        this.client = client;
    }

    public SuppressionQuery search(String query) {
        params.q(query);
        return this;
    }

    public SuppressionQuery reason(SuppressionReason reason) {
        params.put("reason", reason == null ? null : reason.wire());
        return this;
    }

    public SuppressionQuery pageSize(int pageSize) {
        params.pageSize(pageSize);
        return this;
    }

    public SuppressionQuery pageToken(String pageToken) {
        params.pageToken(pageToken);
        return this;
    }

    public Paged<TenantSuppression> list() throws IOException {
        return client.list(params);
    }
}
