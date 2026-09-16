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

    /**
     * Creates a client for the given routing-rule collection path.
     *
     * @param client underlying admin client
     * @param basePath client-relative routing-rule collection path
     */
    public RoutingRulesClient(AdminClient client, String basePath) {
        this.client = client;
        this.basePath = basePath;
    }

    /**
     * Starts a typed, fluent list request.
     *
     * @return a new routing-rule query
     */
    public RoutingRuleQuery query() {
        return new RoutingRuleQuery(this);
    }

    /**
     * Lists routing rules for the listener.
     *
     * @return a lazily paginated collection of routing rules
     * @throws IOException if the request fails or is interrupted
     */
    public Paged<RoutingRule> list() throws IOException {
        return list(null);
    }

    Paged<RoutingRule> list(QueryParams params) throws IOException {
        return client.paged(basePath, params == null ? null : params.toMap(), RoutingRuleData.class)
                .map(data -> new RoutingRule(client, basePath + "/" + data.id(), data));
    }

    /**
     * Creates a routing rule configured through the given builder consumer.
     *
     * @param configure consumer that populates the creation request builder
     * @return the created routing rule
     * @throws IOException if the request fails or is interrupted
     */
    public RoutingRule create(Consumer<CreateRoutingRuleRequest.Builder> configure) throws IOException {
        CreateRoutingRuleRequest.Builder builder = CreateRoutingRuleRequest.builder();
        configure.accept(builder);
        return create(builder.build());
    }

    /**
     * Creates a routing rule described by the given request.
     *
     * @param request routing-rule creation request
     * @return the created routing rule
     * @throws IOException if the request fails or is interrupted
     */
    public RoutingRule create(CreateRoutingRuleRequest request) throws IOException {
        RoutingRuleData data = client.post(basePath, request).as(RoutingRuleData.class);
        return new RoutingRule(client, basePath + "/" + data.id(), data);
    }

    /**
     * Gets a routing rule by its identifier.
     *
     * @param ruleId routing-rule identifier
     * @return the routing rule
     * @throws IOException if the request fails or is interrupted
     */
    public RoutingRule get(String ruleId) throws IOException {
        String resourcePath = basePath + "/" + ruleId;
        return new RoutingRule(client, resourcePath, client.get(resourcePath).as(RoutingRuleData.class));
    }
}
