package com.mxraven.admin.model;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Entity;

/**
 * A submission-listener API key without secret material.
 *
 * <p>The plaintext secret is only ever returned by the create operation as
 * {@link IssuedAPIKey}. There is no per-key read; only deletion.
 */
public final class APIKey extends Entity<APIKeyData> {
    /**
     * Creates an API key entity bound to a client, path, and backing data.
     *
     * @param client admin client used for requests
     * @param path resource path
     * @param data backing API key data
     */
    public APIKey(AdminClient client, String path, APIKeyData data) {
        super(client, path, data);
    }

    /**
     * Returns the API key identifier.
     *
     * @return the API key identifier
     */
    public String id() {
        return data.id();
    }

    /**
     * Returns the owning tenant identifier.
     *
     * @return the owning tenant identifier
     */
    public String tenantId() {
        return data.tenantId();
    }

    /**
     * Returns the listener the key is scoped to.
     *
     * @return the listener the key is scoped to
     */
    public String listenerId() {
        return data.listenerId();
    }

    /**
     * Returns the submission username associated with the key.
     *
     * @return the submission username associated with the key
     */
    public String username() {
        return data.username();
    }
}
