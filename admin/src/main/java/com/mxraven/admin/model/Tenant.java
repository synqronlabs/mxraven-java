package com.mxraven.admin.model;

/**
 * A tenant and its identity-provisioning state.
 *
 * <p>{@code status} is one of {@code active} or {@code suspended}.
 * {@code provisioningState} is one of {@code pending}, {@code provisioning},
 * {@code ready}, {@code failed}, {@code deleting}, or {@code deleted}.
 */
public record Tenant(
        String id,
        String slug,
        TenantStatus status,
        String suspendedAt,
        String suspendedBy,
        String suspensionReason,
        TenantProvisioningState provisioningState,
        String zitadelOrganizationId,
        String zitadelDefaultUserId,
        long desiredGeneration,
        long appliedGeneration,
        String lastSyncedAt,
        String nextReconcileAt,
        String lastReconcileAttemptAt,
        String lastReconcileTrigger) {
}
