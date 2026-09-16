package com.mxraven.admin.client;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.model.TenantQuota;

import java.io.IOException;

/**
 * Effective tenant quotas and usage.
 */
public final class QuotasClient {
    private final AdminClient client;
    private final String tenantSlug;

    /**
     * Creates a client bound to the given tenant.
     *
     * @param client underlying admin client
     * @param tenantSlug tenant slug
     */
    public QuotasClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    /**
     * Gets the effective quota state and usage for a tenant.
     *
     * @return the effective quota state, usage, and over-limit flags
     * @throws IOException if the request fails or is interrupted
     */
    public TenantQuota get() throws IOException {
        return client.get("/tenants/" + tenantSlug + "/quotas").as(TenantQuota.class);
    }
}
