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

    /**
     * Create a client for the tenant-level MTA rate-limit override.
     *
     * @param client underlying admin client
     * @param tenantSlug tenant slug whose override is accessed
     */
    public MtaRateLimitsClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    /**
     * Get the tenant's MTA rate-limit override, including the inherited policy.
     *
     * @return the current tenant rate-limit override
     * @throws IOException if the request fails or is interrupted
     */
    public MTARateLimitOverride get() throws IOException {
        return client.get(tenantPath()).as(MTARateLimitOverride.class);
    }

    /**
     * Replace the tenant's MTA rate-limit override. The policy must not be looser
     * than the currently inherited limits.
     *
     * @param policy replacement policy; only tighter than the inherited limits
     * @return the updated tenant rate-limit override
     * @throws IOException if the request fails or is interrupted
     * @throws IllegalArgumentException if the policy is looser than the inherited
     *                                  limits
     */
    public MTARateLimitOverride put(MTARateLimitPolicy policy) throws IOException {
        MTARateLimitOverride current = get();
        policy.validateStricterThan(current.inherited());
        return client.put(tenantPath(), policy).as(MTARateLimitOverride.class);
    }

    /**
     * Remove the tenant's MTA rate-limit override so it inherits again.
     *
     * @throws IOException if the request fails or is interrupted
     */
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
