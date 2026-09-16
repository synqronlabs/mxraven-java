package com.mxraven.admin.model;

/**
 * Wire representation backing the {@link TenantSuppression} entity.
 *
 * @param tenantId identifier of the owning tenant
 * @param emailAddress suppressed email address
 * @param reason reason the address is suppressed
 */
public record TenantSuppressionData(
        String tenantId,
        String emailAddress,
        SuppressionReason reason) {
}
