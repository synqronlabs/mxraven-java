package com.mxraven.admin.model;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Entity;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Hydrated TenantIdentityProvider entity. Reads return a snapshot; call <code>reload()</code> for fresh state.
 */
public final class TenantIdentityProvider extends Entity<TenantIdentityProviderData> {
    public TenantIdentityProvider(AdminClient client, String path, TenantIdentityProviderData data) {
        super(client, path, data);
    }

    private TenantIdentityProvider(AdminClient client, String path, TenantIdentityProviderData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    public String id() {
        return data.id();
    }

    public String tenantId() {
        return data.tenantId();
    }

    public String idpRef() {
        return data.idpRef();
    }

    public String displayName() {
        return data.displayName();
    }

    public IdentityProviderType providerType() {
        return data.providerType();
    }

    public String zitadelProviderId() {
        return data.zitadelProviderId();
    }

    public String issuer() {
        return data.issuer();
    }

    public String clientId() {
        return data.clientId();
    }

    public List<String> scopes() {
        return data.scopes();
    }

    public Map<String, Object> providerConfig() {
        return data.providerConfig();
    }

    public boolean autoGrantRoles() {
        return data.autoGrantRoles();
    }

    public boolean isActive() {
        return data.isActive();
    }

    public IdentityProviderLifecycleStatus lifecycleStatus() {
        return data.lifecycleStatus();
    }

    public TenantIdentityProvider reload() throws IOException {
        return new TenantIdentityProvider(client, path,
                client.get(path).as(TenantIdentityProviderData.class), isDeleted());
    }

    /** Enable or disable automatic role grants for this provider. */
    public TenantIdentityProvider setAutoGrant(boolean enabled) throws IOException {
        return new TenantIdentityProvider(client, path,
                client.put(path + "/auto-grant", new UpdateIdentityProviderAutoGrantRequest(enabled))
                        .as(TenantIdentityProviderData.class), isDeleted());
    }
}
