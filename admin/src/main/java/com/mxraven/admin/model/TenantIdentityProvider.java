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
    /**
     * Creates a provider bound to the given client, path, and data.
     *
     * @param client the admin client
     * @param path   the resource path
     * @param data   the backing data
     */
    public TenantIdentityProvider(AdminClient client, String path, TenantIdentityProviderData data) {
        super(client, path, data);
    }

    private TenantIdentityProvider(AdminClient client, String path, TenantIdentityProviderData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    /**
     * Returns the entity identifier.
     *
     * @return the entity identifier
     */
    public String id() {
        return data.id();
    }

    /**
     * Returns the identifier of the owning tenant.
     *
     * @return the owning tenant identifier
     */
    public String tenantId() {
        return data.tenantId();
    }

    /**
     * Returns the stable reference for the provider.
     *
     * @return the provider reference
     */
    public String idpRef() {
        return data.idpRef();
    }

    /**
     * Returns the human-readable provider name.
     *
     * @return the provider name
     */
    public String displayName() {
        return data.displayName();
    }

    /**
     * Returns the identity provider type.
     *
     * @return the provider type
     */
    public IdentityProviderType providerType() {
        return data.providerType();
    }

    /**
     * Returns the provider identifier in Zitadel.
     *
     * @return the Zitadel provider identifier
     */
    public String zitadelProviderId() {
        return data.zitadelProviderId();
    }

    /**
     * Returns the token issuer.
     *
     * @return the issuer
     */
    public String issuer() {
        return data.issuer();
    }

    /**
     * Returns the OAuth or OIDC client identifier.
     *
     * @return the client identifier
     */
    public String clientId() {
        return data.clientId();
    }

    /**
     * Returns the requested scopes.
     *
     * @return the requested scopes
     */
    public List<String> scopes() {
        return data.scopes();
    }

    /**
     * Returns the provider-specific configuration.
     *
     * @return the provider configuration
     */
    public Map<String, Object> providerConfig() {
        return data.providerConfig();
    }

    /**
     * Returns whether roles are granted automatically.
     *
     * @return {@code true} if roles are granted automatically
     */
    public boolean autoGrantRoles() {
        return data.autoGrantRoles();
    }

    /**
     * Returns whether the provider is active.
     *
     * @return {@code true} if the provider is active
     */
    public boolean isActive() {
        return data.isActive();
    }

    /**
     * Returns the provider lifecycle status.
     *
     * @return the lifecycle status
     */
    public IdentityProviderLifecycleStatus lifecycleStatus() {
        return data.lifecycleStatus();
    }

    /**
     * Re-fetches this provider and returns a fresh snapshot.
     *
     * @return a fresh provider snapshot
     * @throws IOException if the request fails
     */
    public TenantIdentityProvider reload() throws IOException {
        return new TenantIdentityProvider(client, path,
                client.get(path).as(TenantIdentityProviderData.class), isDeleted());
    }

    /**
     * Enable or disable automatic role grants for this provider.
     *
     * @param enabled whether roles are granted automatically
     * @return the updated provider
     * @throws IOException if the request fails
     */
    public TenantIdentityProvider setAutoGrant(boolean enabled) throws IOException {
        return new TenantIdentityProvider(client, path,
                client.put(path + "/auto-grant", new UpdateIdentityProviderAutoGrantRequest(enabled))
                        .as(TenantIdentityProviderData.class), isDeleted());
    }
}
