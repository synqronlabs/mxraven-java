package com.mxraven.admin.model;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Entity;
import com.mxraven.admin.client.WebhookDeliveriesClient;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Hydrated WebhookEndpoint entity. Reads return a snapshot; call <code>reload()</code> for fresh state.
 */
public final class WebhookEndpoint extends Entity<WebhookEndpointData> {
    /**
     * Creates an endpoint bound to the given client, path, and data.
     *
     * @param client the admin client
     * @param path   the resource path
     * @param data   the backing data
     */
    public WebhookEndpoint(AdminClient client, String path, WebhookEndpointData data) {
        super(client, path, data);
    }

    private WebhookEndpoint(AdminClient client, String path, WebhookEndpointData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    /**
     * Returns the entity identifier.
     *
     * @return the entity identifier
     */
    public String id() {
        return data.id();
    }

    /**
     * Returns the identifier of the owning tenant.
     *
     * @return the owning tenant identifier
     */
    public String tenantId() {
        return data.tenantId();
    }

    /**
     * Returns the stable reference for the endpoint.
     *
     * @return the endpoint reference
     */
    public String webhookRef() {
        return data.webhookRef();
    }

    /**
     * Returns the human-readable endpoint name.
     *
     * @return the endpoint name
     */
    public String displayName() {
        return data.displayName();
    }

    /**
     * Returns the HTTPS URL that deliveries are sent to.
     *
     * @return the delivery target URL
     */
    public String targetUrl() {
        return data.targetUrl();
    }

    /**
     * Returns the key identifier used to sign deliveries.
     *
     * @return the signing key identifier
     */
    public String signingKid() {
        return data.signingKid();
    }

    /**
     * Returns whether a signing secret is stored.
     *
     * @return {@code true} if a signing secret is stored
     */
    public boolean hasSigningSecret() {
        return data.hasSigningSecret();
    }

    /**
     * Returns whether the endpoint is active.
     *
     * @return {@code true} if the endpoint is active
     */
    public boolean isActive() {
        return data.isActive();
    }

    /**
     * Returns the timestamp when the endpoint was created.
     *
     * @return the creation timestamp
     */
    public String createdAt() {
        return data.createdAt();
    }

    /**
     * Returns the timestamp when the endpoint was last updated.
     *
     * @return the last-update timestamp
     */
    public String updatedAt() {
        return data.updatedAt();
    }

    /**
     * Re-fetches this endpoint and returns a fresh snapshot.
     *
     * @return a fresh endpoint snapshot
     * @throws IOException if the request fails
     */
    public WebhookEndpoint reload() throws IOException {
        return new WebhookEndpoint(client, path, client.get(path).as(WebhookEndpointData.class), isDeleted());
    }

    /**
     * Replaces this endpoint's configuration using a builder.
     *
     * @param configure callback that configures the update request
     * @return the updated endpoint
     * @throws IOException if the request fails
     */
    public WebhookEndpoint update(Consumer<UpdateWebhookEndpointRequest.Builder> configure) throws IOException {
        UpdateWebhookEndpointRequest.Builder builder = UpdateWebhookEndpointRequest.builder();
        configure.accept(builder);
        return update(builder.build());
    }

    /**
     * Replaces this endpoint's configuration.
     *
     * @param request replacement request
     * @return the updated endpoint
     * @throws IOException if the request fails
     */
    public WebhookEndpoint update(UpdateWebhookEndpointRequest request) throws IOException {
        return new WebhookEndpoint(client, path, client.put(path, request).as(WebhookEndpointData.class), isDeleted());
    }

    /**
     * Enables or disables the endpoint.
     *
     * @param isActive whether the endpoint is active
     * @return the updated endpoint
     * @throws IOException if the request fails
     */
    public WebhookEndpoint setActive(boolean isActive) throws IOException {
        return setActive(new SetIntegrationActiveRequest(isActive));
    }

    /**
     * Enables or disables the endpoint using a builder.
     *
     * @param configure callback that configures the active-state request
     * @return the updated endpoint
     * @throws IOException if the request fails
     */
    public WebhookEndpoint setActive(Consumer<SetIntegrationActiveRequest.Builder> configure) throws IOException {
        SetIntegrationActiveRequest.Builder builder = SetIntegrationActiveRequest.builder();
        configure.accept(builder);
        return setActive(builder.build());
    }

    /**
     * Sets the endpoint active state.
     *
     * @param request active-state request
     * @return the updated endpoint
     * @throws IOException if the request fails
     */
    public WebhookEndpoint setActive(SetIntegrationActiveRequest request) throws IOException {
        return new WebhookEndpoint(client, path,
                client.put(path + "/active", request).as(WebhookEndpointData.class), isDeleted());
    }

    /**
     * Replaces the signing key and returns the new one-time signing secret.
     *
     * @return the issued endpoint carrying the new signing secret
     * @throws IOException if the request fails
     */
    public IssuedWebhookEndpoint rotateSecret() throws IOException {
        return client.post(path + "/rotate-secret", null).as(IssuedWebhookEndpoint.class);
    }

    /**
     * Returns the client for this endpoint's deliveries.
     *
     * @return the deliveries client
     */
    public WebhookDeliveriesClient deliveries() {
        return new WebhookDeliveriesClient(client, path + "/deliveries");
    }
}
