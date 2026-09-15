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

    public QuotasClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    /** Get the effective quota state and usage for a tenant. */
    public TenantQuota get() throws IOException {
        return client.get("/tenants/" + tenantSlug + "/quotas").as(TenantQuota.class);
    }
}
