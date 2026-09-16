package com.mxraven.admin.model;

/**
 * Public login context for a tenant.
 *
 * @param tenantSlug slug of the tenant
 * @param workspaceRef reference of the tenant workspace
 * @param displayName human-readable tenant name
 * @param zitadelOrganizationId Zitadel organization identifier
 */
public record TenantLoginContext(
        String tenantSlug,
        String workspaceRef,
        String displayName,
        String zitadelOrganizationId) {
}
