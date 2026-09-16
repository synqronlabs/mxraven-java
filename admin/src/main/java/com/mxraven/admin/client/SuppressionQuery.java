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

    /**
     * Sets the {@code q} search filter for suppressions.
     *
     * @param query search text; {@code null} or blank is ignored
     * @return this query
     * @throws IllegalArgumentException if the query is non-blank and shorter than
     *                                  {@value QueryParams#MIN_SEARCH_LENGTH} characters
     */
    public SuppressionQuery search(String query) {
        params.q(query);
        return this;
    }

    /**
     * Restricts results to a single suppression reason.
     *
     * @param reason suppression reason to match; {@code null} is ignored
     * @return this query
     */
    public SuppressionQuery reason(SuppressionReason reason) {
        params.put("reason", reason == null ? null : reason.wire());
        return this;
    }

    /**
     * Sets the maximum number of suppressions per page.
     *
     * @param pageSize number of items per page, between 1 and 500
     * @return this query
     * @throws IllegalArgumentException if {@code pageSize} is outside the range 1 to 500
     */
    public SuppressionQuery pageSize(int pageSize) {
        params.pageSize(pageSize);
        return this;
    }

    /**
     * Sets the opaque page token to continue from.
     *
     * @param pageToken opaque page token; {@code null} is ignored
     * @return this query
     */
    public SuppressionQuery pageToken(String pageToken) {
        params.pageToken(pageToken);
        return this;
    }

    /**
     * Lists suppressions matching the configured filters.
     *
     * @return a lazily paginated collection of tenant suppressions
     * @throws IOException if the request fails or is interrupted
     */
    public Paged<TenantSuppression> list() throws IOException {
        return client.list(params);
    }
}
