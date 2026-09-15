package com.mxraven.admin.client;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.APIKey;
import com.mxraven.admin.model.APIKeyData;
import com.mxraven.admin.model.IssuedAPIKey;

import java.io.IOException;

/**
 * Submission-listener API keys, obtained from
 * {@link com.mxraven.admin.model.Listener#apiKeys()}.
 *
 * <p>Listing never returns secret material. {@link #create()} returns the
 * plaintext secret exactly once as an {@link IssuedAPIKey}; it cannot be
 * retrieved again.
 */
public final class ApiKeysClient {
    private final AdminClient client;
    private final String basePath;

    public ApiKeysClient(AdminClient client, String basePath) {
        this.client = client;
        this.basePath = basePath;
    }

    public Paged<APIKey> list() throws IOException {
        return list(null);
    }

    Paged<APIKey> list(QueryParams params) throws IOException {
        return client.paged(basePath, params == null ? null : params.toMap(), APIKeyData.class)
                .map(data -> new APIKey(client, basePath + "/" + data.id(), data));
    }

    /**
     * Issues an API key and returns its one-time plaintext secret. This operation
     * is non-idempotent; do not blind-retry an uncertain response.
     */
    public IssuedAPIKey create() throws IOException {
        return client.post(basePath, null).as(IssuedAPIKey.class);
    }
}
