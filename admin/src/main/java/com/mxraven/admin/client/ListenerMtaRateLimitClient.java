package com.mxraven.admin.client;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.model.MTARateLimitOverride;
import com.mxraven.admin.model.MTARateLimitPolicy;

import java.io.IOException;

/**
 * The MTA rate-limit override for a single listener, obtained from
 * {@link com.mxraven.admin.model.Listener#mtaRateLimit()}.
 */
public final class ListenerMtaRateLimitClient {
    private final AdminClient client;
    private final String path;

    /**
     * Create a client for a single listener's MTA rate-limit override.
     *
     * @param client underlying admin client
     * @param path client-relative path of the override resource
     */
    public ListenerMtaRateLimitClient(AdminClient client, String path) {
        this.client = client;
        this.path = path;
    }

    /**
     * Get the listener's MTA rate-limit override, including the inherited policy.
     *
     * @return the current listener rate-limit override
     * @throws IOException if the request fails or is interrupted
     */
    public MTARateLimitOverride get() throws IOException {
        return client.get(path).as(MTARateLimitOverride.class);
    }

    /**
     * Replace the listener's MTA rate-limit override. The policy must not be
     * looser than the currently inherited limits.
     *
     * @param policy replacement policy; only tighter than the inherited limits
     * @return the updated listener rate-limit override
     * @throws IOException if the request fails or is interrupted
     * @throws IllegalArgumentException if the policy is looser than the inherited
     *                                  limits
     */
    public MTARateLimitOverride put(MTARateLimitPolicy policy) throws IOException {
        MTARateLimitOverride current = get();
        policy.validateStricterThan(current.inherited());
        return client.put(path, policy).as(MTARateLimitOverride.class);
    }

    /**
     * Remove the listener's MTA rate-limit override so it inherits again.
     *
     * @throws IOException if the request fails or is interrupted
     */
    public void delete() throws IOException {
        client.delete(path);
    }
}
