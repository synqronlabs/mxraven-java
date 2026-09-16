package com.mxraven.admin.model;

import java.util.List;
import java.util.Map;

/**
 * Wire representation backing the {@link TenantIdentityProvider} entity.
 *
 * @param id unique provider identifier
 * @param tenantId identifier of the owning tenant
 * @param idpRef stable reference for the provider
 * @param displayName human-readable provider name
 * @param providerType identity provider type
 * @param zitadelProviderId provider identifier in Zitadel
 * @param issuer token issuer
 * @param clientId OAuth or OIDC client identifier
 * @param scopes requested scopes
 * @param providerConfig provider-specific configuration
 * @param autoGrantRoles whether roles are granted automatically
 * @param isActive whether the provider is active
 * @param lifecycleStatus provider lifecycle status
 */
public record TenantIdentityProviderData(
        String id,
        String tenantId,
        String idpRef,
        String displayName,
        IdentityProviderType providerType,
        String zitadelProviderId,
        String issuer,
        String clientId,
        List<String> scopes,
        Map<String, Object> providerConfig,
        boolean autoGrantRoles,
        boolean isActive,
        IdentityProviderLifecycleStatus lifecycleStatus) {
}
