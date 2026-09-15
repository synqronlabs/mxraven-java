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

    public ListenerMtaRateLimitClient(AdminClient client, String path) {
        this.client = client;
        this.path = path;
    }

    public MTARateLimitOverride get() throws IOException {
        return client.get(path).as(MTARateLimitOverride.class);
    }

    public MTARateLimitOverride put(MTARateLimitPolicy policy) throws IOException {
        MTARateLimitOverride current = get();
        policy.validateStricterThan(current.inherited());
        return client.put(path, policy).as(MTARateLimitOverride.class);
    }

    public void delete() throws IOException {
        client.delete(path);
    }
}
