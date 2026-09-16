package com.mxraven.admin.model;

/**
 * A tenant and its identity-provisioning state.
 *
 * <p>{@code status} is one of {@code active} or {@code suspended}.
 * {@code provisioningState} is one of {@code pending}, {@code provisioning},
 * {@code ready}, {@code failed}, {@code deleting}, or {@code deleted}.
 *
 * @param id unique tenant identifier
 * @param slug URL-friendly tenant slug
 * @param status tenant lifecycle status
 * @param suspendedAt timestamp when the tenant was suspended
 * @param suspendedBy actor that suspended the tenant
 * @param suspensionReason reason the tenant was suspended
 * @param provisioningState identity-provisioning state
 * @param zitadelOrganizationId Zitadel organization identifier
 * @param zitadelDefaultUserId Zitadel default user identifier
 * @param desiredGeneration desired identity-provisioning generation
 * @param appliedGeneration applied identity-provisioning generation
 * @param lastSyncedAt timestamp of the last successful sync
 * @param nextReconcileAt timestamp of the next scheduled reconcile
 * @param lastReconcileAttemptAt timestamp of the last reconcile attempt
 * @param lastReconcileTrigger trigger of the last reconcile attempt
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
