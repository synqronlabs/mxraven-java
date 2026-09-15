package com.mxraven.admin.client;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.WebhookDelivery;

import java.io.IOException;

/**
 * Delivery history for a single webhook endpoint, obtained from
 * {@link WebhookEndpointRef#deliveries()}.
 */
public final class WebhookDeliveriesClient {
    private final AdminClient client;
    private final String path;

    public WebhookDeliveriesClient(AdminClient client, String path) {
        this.client = client;
        this.path = path;
    }

    public Paged<WebhookDelivery> list() throws IOException {
        return list(null);
    }

    Paged<WebhookDelivery> list(QueryParams params) throws IOException {
        return client.paged(path, params == null ? null : params.toMap(), WebhookDelivery.class);
    }
}
