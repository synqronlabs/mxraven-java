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
    public WebhookEndpoint(AdminClient client, String path, WebhookEndpointData data) {
        super(client, path, data);
    }

    private WebhookEndpoint(AdminClient client, String path, WebhookEndpointData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    public String id() {
        return data.id();
    }

    public String tenantId() {
        return data.tenantId();
    }

    public String webhookRef() {
        return data.webhookRef();
    }

    public String displayName() {
        return data.displayName();
    }

    public String targetUrl() {
        return data.targetUrl();
    }

    public String signingKid() {
        return data.signingKid();
    }

    public boolean hasSigningSecret() {
        return data.hasSigningSecret();
    }

    public boolean isActive() {
        return data.isActive();
    }

    public String createdAt() {
        return data.createdAt();
    }

    public String updatedAt() {
        return data.updatedAt();
    }

    public WebhookEndpoint reload() throws IOException {
        return new WebhookEndpoint(client, path, client.get(path).as(WebhookEndpointData.class), isDeleted());
    }

    public WebhookEndpoint update(Consumer<UpdateWebhookEndpointRequest.Builder> configure) throws IOException {
        UpdateWebhookEndpointRequest.Builder builder = UpdateWebhookEndpointRequest.builder();
        configure.accept(builder);
        return update(builder.build());
    }

    public WebhookEndpoint update(UpdateWebhookEndpointRequest request) throws IOException {
        return new WebhookEndpoint(client, path, client.put(path, request).as(WebhookEndpointData.class), isDeleted());
    }

    public WebhookEndpoint setActive(boolean isActive) throws IOException {
        return setActive(new SetIntegrationActiveRequest(isActive));
    }

    public WebhookEndpoint setActive(Consumer<SetIntegrationActiveRequest.Builder> configure) throws IOException {
        SetIntegrationActiveRequest.Builder builder = SetIntegrationActiveRequest.builder();
        configure.accept(builder);
        return setActive(builder.build());
    }

    public WebhookEndpoint setActive(SetIntegrationActiveRequest request) throws IOException {
        return new WebhookEndpoint(client, path,
                client.put(path + "/active", request).as(WebhookEndpointData.class), isDeleted());
    }

    /** Replaces the signing key and returns the new one-time signing secret. */
    public IssuedWebhookEndpoint rotateSecret() throws IOException {
        return client.post(path + "/rotate-secret", null).as(IssuedWebhookEndpoint.class);
    }

    public WebhookDeliveriesClient deliveries() {
        return new WebhookDeliveriesClient(client, path + "/deliveries");
    }
}
