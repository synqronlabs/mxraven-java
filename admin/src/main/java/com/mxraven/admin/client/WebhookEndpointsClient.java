package com.mxraven.admin.client;

import com.mxraven.admin.internal.Java8;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.CreateWebhookEndpointRequest;
import com.mxraven.admin.model.IssuedWebhookEndpoint;
import com.mxraven.admin.model.WebhookEndpoint;
import com.mxraven.admin.model.WebhookEndpointData;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * Tenant webhook-endpoint collection. Reads return hydrated
 * {@link WebhookEndpoint} entities; creation returns the endpoint together with
 * its one-time signing secret.
 */
public final class WebhookEndpointsClient {
    private final AdminClient client;
    private final String tenantSlug;

    /**
     * Creates a client bound to the given tenant.
     *
     * @param client underlying admin client
     * @param tenantSlug tenant slug
     */
    public WebhookEndpointsClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    /**
     * Lists webhook endpoints for the tenant.
     *
     * @return a lazily paginated collection of webhook endpoints
     * @throws IOException if the request fails or is interrupted
     */
    public Paged<WebhookEndpoint> list() throws IOException {
        return list(null);
    }

    Paged<WebhookEndpoint> list(QueryParams params) throws IOException {
        return client.paged(base(), params == null ? null : params.toMap(), WebhookEndpointData.class)
                .map(data -> new WebhookEndpoint(client, one(data.id()), data));
    }

    /**
     * Returns the endpoint together with its one-time signing secret.
     *
     * @param configure consumer that populates the creation request builder
     * @return the created endpoint and its one-time signing secret
     * @throws IOException if the request fails or is interrupted
     */
    public IssuedWebhookEndpoint create(Consumer<CreateWebhookEndpointRequest.Builder> configure) throws IOException {
        CreateWebhookEndpointRequest.Builder builder = CreateWebhookEndpointRequest.builder();
        configure.accept(builder);
        return create(builder.build());
    }

    /**
     * Creates an endpoint described by the given request.
     *
     * @param request webhook-endpoint creation request
     * @return the created endpoint and its one-time signing secret
     * @throws IOException if the request fails or is interrupted
     */
    public IssuedWebhookEndpoint create(CreateWebhookEndpointRequest request) throws IOException {
        return client.post(base(), request).as(IssuedWebhookEndpoint.class);
    }

    private String base() {
        return "/tenants/" + seg(tenantSlug) + "/webhook-endpoints";
    }

    private String one(String id) {
        return base() + "/" + seg(id);
    }

    private static String seg(String value) {
        return Java8.urlEncode(value);
    }

    /**
     * Gets a webhook endpoint by its identifier.
     *
     * @param endpointId webhook-endpoint identifier
     * @return the webhook endpoint
     * @throws IOException if the request fails or is interrupted
     */
    public WebhookEndpoint get(String endpointId) throws IOException {
        String resourcePath = one(endpointId);
        return new WebhookEndpoint(client, resourcePath, client.get(resourcePath).as(WebhookEndpointData.class));
    }

    /**
     * Gets a webhook endpoint by its immutable {@code webhook_ref}.
     *
     * @param webhookRef immutable webhook-endpoint reference
     * @return the webhook endpoint
     * @throws IOException if the request fails or is interrupted
     */
    public WebhookEndpoint getByRef(String webhookRef) throws IOException {
        String refPath = base() + "/ref/" + seg(webhookRef);
        WebhookEndpointData data = client.get(refPath).as(WebhookEndpointData.class);
        return new WebhookEndpoint(client, one(data.id()), data);
    }
}
