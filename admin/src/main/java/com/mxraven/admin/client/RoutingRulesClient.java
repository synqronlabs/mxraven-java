package com.mxraven.admin.client;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.CreateRoutingRuleRequest;
import com.mxraven.admin.model.RoutingRule;
import com.mxraven.admin.model.RoutingRuleData;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Routing rules for a single listener, obtained from
 * {@link com.mxraven.admin.model.Listener#routingRules()}.
 */
public final class RoutingRulesClient {
    private final AdminClient client;
    private final String basePath;

    public RoutingRulesClient(AdminClient client, String basePath) {
        this.client = client;
        this.basePath = basePath;
    }

    /** Start a typed, fluent list request. */
    public RoutingRuleQuery query() {
        return new RoutingRuleQuery(this);
    }

    public Paged<RoutingRule> list() throws IOException {
        return list(null);
    }

    Paged<RoutingRule> list(QueryParams params) throws IOException {
        return client.paged(basePath, params == null ? null : params.toMap(), RoutingRuleData.class)
                .map(data -> new RoutingRule(client, basePath + "/" + data.id(), data));
    }

    public RoutingRule create(Consumer<CreateRoutingRuleRequest.Builder> configure) throws IOException {
        CreateRoutingRuleRequest.Builder builder = CreateRoutingRuleRequest.builder();
        configure.accept(builder);
        return create(builder.build());
    }

    public RoutingRule create(CreateRoutingRuleRequest request) throws IOException {
        RoutingRuleData data = client.post(basePath, request).as(RoutingRuleData.class);
        return new RoutingRule(client, basePath + "/" + data.id(), data);
    }

    public RoutingRule get(String ruleId) throws IOException {
        String resourcePath = basePath + "/" + ruleId;
        return new RoutingRule(client, resourcePath, client.get(resourcePath).as(RoutingRuleData.class));
    }
}
