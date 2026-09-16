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

    /**
     * Create a client for the API-key collection at the given path.
     *
     * @param client underlying admin client
     * @param basePath client-relative path of the API-key collection
     */
    public ApiKeysClient(AdminClient client, String basePath) {
        this.client = client;
        this.basePath = basePath;
    }

    /**
     * List the API keys (first page fetched eagerly, remaining pages lazy).
     *
     * <p>Secret material is never returned by listing.
     *
     * @return a lazily paginating collection of API keys
     * @throws IOException if the first page request fails or is interrupted
     */
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
     *
     * @return the issued key, including its one-time plaintext secret
     * @throws IOException if the request fails or is interrupted
     */
    public IssuedAPIKey create() throws IOException {
        return client.post(basePath, null).as(IssuedAPIKey.class);
    }
}
