package com.mxraven.admin.client;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.model.TenantLoginContext;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Public authentication-context operations under {@code /v2/auth}.
 */
public final class AuthClient {
    private final AdminClient client;

    public AuthClient(AdminClient client) {
        this.client = client;
    }

    /**
     * Resolve the public login context for a tenant.
     *
     * <p>Unknown, unavailable, suspended, deleted, and unprovisioned tenants all
     * return the same not-found problem. The tenant path segment is URL-encoded.
     */
    public TenantLoginContext loginContext(String tenantSlug) throws IOException {
        return client.get("/auth/tenants/" + URLEncoder.encode(tenantSlug, StandardCharsets.UTF_8)
                + "/login-context").as(TenantLoginContext.class);
    }
}
