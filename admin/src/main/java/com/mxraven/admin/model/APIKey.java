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
    public APIKey(AdminClient client, String path, APIKeyData data) {
        super(client, path, data);
    }

    public String id() {
        return data.id();
    }

    public String tenantId() {
        return data.tenantId();
    }

    public String listenerId() {
        return data.listenerId();
    }

    public String username() {
        return data.username();
    }
}
