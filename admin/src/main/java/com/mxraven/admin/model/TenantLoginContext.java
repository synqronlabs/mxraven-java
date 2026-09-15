package com.mxraven.admin.model;

/**
 * Public login context for a tenant.
 */
public record TenantLoginContext(
        String tenantSlug,
        String workspaceRef,
        String displayName,
        String zitadelOrganizationId) {
}
