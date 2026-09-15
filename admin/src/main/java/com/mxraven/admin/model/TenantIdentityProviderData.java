package com.mxraven.admin.model;

import java.util.List;
import java.util.Map;

/** Wire representation backing the {@link TenantIdentityProvider} entity. */
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
