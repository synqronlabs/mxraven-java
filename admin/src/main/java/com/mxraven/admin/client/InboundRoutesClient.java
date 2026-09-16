package com.mxraven.admin.client;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.CreateInboundRouteRequest;
import com.mxraven.admin.model.InboundRoute;
import com.mxraven.admin.model.InboundRouteData;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * Tenant inbound-route collection. Reads and creation return hydrated
 * {@link InboundRoute} entities.
 */
public final class InboundRoutesClient {
    private final AdminClient client;
    private final String tenantSlug;

    /**
     * Create a client for the inbound-route collection of a tenant.
     *
     * @param client underlying admin client
     * @param tenantSlug tenant slug whose inbound routes are accessed
     */
    public InboundRoutesClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    /**
     * Start a typed, fluent list request.
     *
     * @return a new inbound-route query
     */
    public InboundRouteQuery query() {
        return new InboundRouteQuery(this);
    }

    /**
     * List the inbound routes (first page fetched eagerly, remaining pages lazy).
     *
     * @return a lazily paginating collection of inbound routes
     * @throws IOException if the first page request fails or is interrupted
     */
    public Paged<InboundRoute> list() throws IOException {
        return list(null);
    }

    Paged<InboundRoute> list(QueryParams params) throws IOException {
        return client.paged(base(), params == null ? null : params.toMap(), InboundRouteData.class)
                .map(data -> new InboundRoute(client, one(data.id()), data));
    }

    /**
     * Create an inbound route from a configured builder.
     *
     * @param configure consumer that configures the request builder
     * @return the created inbound route
     * @throws IOException if the request fails or is interrupted
     */
    public InboundRoute create(Consumer<CreateInboundRouteRequest.Builder> configure) throws IOException {
        CreateInboundRouteRequest.Builder builder = CreateInboundRouteRequest.builder();
        configure.accept(builder);
        return create(builder.build());
    }

    /**
     * Create an inbound route.
     *
     * @param request creation request
     * @return the created inbound route
     * @throws IOException if the request fails or is interrupted
     */
    public InboundRoute create(CreateInboundRouteRequest request) throws IOException {
        InboundRouteData data = client.post(base(), request).as(InboundRouteData.class);
        return new InboundRoute(client, one(data.id()), data);
    }

    private String base() {
        return "/tenants/" + tenantSlug + "/inbound-routes";
    }

    private String one(String id) {
        return base() + "/" + encode(id);
    }

    private static String encode(String segment) {
        return URLEncoder.encode(segment, StandardCharsets.UTF_8);
    }

    /**
     * Get an inbound route by its identifier.
     *
     * @param routeId inbound-route identifier
     * @return the hydrated inbound route
     * @throws IOException if the request fails or is interrupted
     */
    public InboundRoute get(String routeId) throws IOException {
        String resourcePath = one(routeId);
        return new InboundRoute(client, resourcePath, client.get(resourcePath).as(InboundRouteData.class));
    }
}
