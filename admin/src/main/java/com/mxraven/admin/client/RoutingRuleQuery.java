package com.mxraven.admin.client;

import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.RoutingRule;
import com.mxraven.admin.model.RoutingRuleActionKind;

import java.io.IOException;

/**
 * Typed filter builder for {@link RoutingRulesClient#query(String)}.
 */
public final class RoutingRuleQuery {
    private final RoutingRulesClient client;
    private final QueryParams params = QueryParams.create();

    RoutingRuleQuery(RoutingRulesClient client) {
        this.client = client;
    }

    public RoutingRuleQuery search(String query) {
        params.q(query);
        return this;
    }

    public RoutingRuleQuery actionType(RoutingRuleActionKind actionType) {
        params.put("action_type", actionType == null ? null : actionType.wire());
        return this;
    }

    public RoutingRuleQuery active(boolean isActive) {
        params.put("is_active", isActive);
        return this;
    }

    public RoutingRuleQuery pageSize(int pageSize) {
        params.pageSize(pageSize);
        return this;
    }

    public RoutingRuleQuery pageToken(String pageToken) {
        params.pageToken(pageToken);
        return this;
    }

    public Paged<RoutingRule> list() throws IOException {
        return client.list(params);
    }
}
