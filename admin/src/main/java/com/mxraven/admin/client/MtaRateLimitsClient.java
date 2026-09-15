package com.mxraven.admin.client;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.model.MTARateLimitOverride;
import com.mxraven.admin.model.MTARateLimitPolicy;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Tenant-level MTA rate-limit override, obtained from
 * {@link com.mxraven.admin.Workspace#mtaRateLimits()}. Listener overrides live
 * on {@link com.mxraven.admin.model.Listener#mtaRateLimit()}.
 */
public final class MtaRateLimitsClient {
    private final AdminClient client;
    private final String tenantSlug;

    public MtaRateLimitsClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    public MTARateLimitOverride get() throws IOException {
        return client.get(tenantPath()).as(MTARateLimitOverride.class);
    }

    public MTARateLimitOverride put(MTARateLimitPolicy policy) throws IOException {
        MTARateLimitOverride current = get();
        policy.validateStricterThan(current.inherited());
        return client.put(tenantPath(), policy).as(MTARateLimitOverride.class);
    }

    public void delete() throws IOException {
        client.delete(tenantPath());
    }

    private String tenantPath() {
        return "/tenants/" + encode(tenantSlug) + "/mta-rate-limit-override";
    }

    private static String encode(String segment) {
        return URLEncoder.encode(segment, StandardCharsets.UTF_8);
    }
}
