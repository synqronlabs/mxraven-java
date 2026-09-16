package com.mxraven.admin.client;

import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.RoutingRule;
import com.mxraven.admin.model.RoutingRuleActionKind;

import java.io.IOException;

/**
 * Typed filter builder for {@link RoutingRulesClient#query()}.
 */
public final class RoutingRuleQuery {
    private final RoutingRulesClient client;
    private final QueryParams params = QueryParams.create();

    RoutingRuleQuery(RoutingRulesClient client) {
        this.client = client;
    }

    /**
     * Sets the {@code q} search filter for routing rules.
     *
     * @param query search text; {@code null} or blank is ignored
     * @return this query
     * @throws IllegalArgumentException if the query is non-blank and shorter than
     *                                  {@value QueryParams#MIN_SEARCH_LENGTH} characters
     */
    public RoutingRuleQuery search(String query) {
        params.q(query);
        return this;
    }

    /**
     * Restricts results to a single routing-rule action kind.
     *
     * @param actionType action kind to match; {@code null} is ignored
     * @return this query
     */
    public RoutingRuleQuery actionType(RoutingRuleActionKind actionType) {
        params.put("action_type", actionType == null ? null : actionType.wire());
        return this;
    }

    /**
     * Restricts results by whether the routing rule is active.
     *
     * @param isActive {@code true} to match active rules, {@code false} for inactive
     * @return this query
     */
    public RoutingRuleQuery active(boolean isActive) {
        params.put("is_active", isActive);
        return this;
    }

    /**
     * Sets the maximum number of routing rules per page.
     *
     * @param pageSize number of items per page, between 1 and 500
     * @return this query
     * @throws IllegalArgumentException if {@code pageSize} is outside the range 1 to 500
     */
    public RoutingRuleQuery pageSize(int pageSize) {
        params.pageSize(pageSize);
        return this;
    }

    /**
     * Sets the opaque page token to continue from.
     *
     * @param pageToken opaque page token; {@code null} is ignored
     * @return this query
     */
    public RoutingRuleQuery pageToken(String pageToken) {
        params.pageToken(pageToken);
        return this;
    }

    /**
     * Lists routing rules matching the configured filters.
     *
     * @return a lazily paginated collection of routing rules
     * @throws IOException if the request fails or is interrupted
     */
    public Paged<RoutingRule> list() throws IOException {
        return client.list(params);
    }
}
