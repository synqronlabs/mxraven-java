package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * A tenant and its identity-provisioning state.
 *
 * <p>{@code status} is one of {@code active} or {@code suspended}.
 * {@code provisioningState} is one of {@code pending}, {@code provisioning},
 * {@code ready}, {@code failed}, {@code deleting}, or {@code deleted}.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class Tenant {
    private final String id;
    private final String slug;
    private final TenantStatus status;
    private final String suspendedAt;
    private final String suspendedBy;
    private final String suspensionReason;
    private final TenantProvisioningState provisioningState;
    private final String zitadelOrganizationId;
    private final String zitadelDefaultUserId;
    private final long desiredGeneration;
    private final long appliedGeneration;
    private final String lastSyncedAt;
    private final String nextReconcileAt;
    private final String lastReconcileAttemptAt;
    private final String lastReconcileTrigger;

    /** unique tenant identifier */
    public String id() {
        return id;
    }

    /** URL-friendly tenant slug */
    public String slug() {
        return slug;
    }

    /** tenant lifecycle status */
    public TenantStatus status() {
        return status;
    }

    /** timestamp when the tenant was suspended */
    public String suspendedAt() {
        return suspendedAt;
    }

    /** actor that suspended the tenant */
    public String suspendedBy() {
        return suspendedBy;
    }

    /** reason the tenant was suspended */
    public String suspensionReason() {
        return suspensionReason;
    }

    /** identity-provisioning state */
    public TenantProvisioningState provisioningState() {
        return provisioningState;
    }

    /** Zitadel organization identifier */
    public String zitadelOrganizationId() {
        return zitadelOrganizationId;
    }

    /** Zitadel default user identifier */
    public String zitadelDefaultUserId() {
        return zitadelDefaultUserId;
    }

    /** desired identity-provisioning generation */
    public long desiredGeneration() {
        return desiredGeneration;
    }

    /** applied identity-provisioning generation */
    public long appliedGeneration() {
        return appliedGeneration;
    }

    /** timestamp of the last successful sync */
    public String lastSyncedAt() {
        return lastSyncedAt;
    }

    /** timestamp of the next scheduled reconcile */
    public String nextReconcileAt() {
        return nextReconcileAt;
    }

    /** timestamp of the last reconcile attempt */
    public String lastReconcileAttemptAt() {
        return lastReconcileAttemptAt;
    }

    /** trigger of the last reconcile attempt */
    public String lastReconcileTrigger() {
        return lastReconcileTrigger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tenant that = (Tenant) o;
        return Objects.equals(this.id, that.id)
                && Objects.equals(this.slug, that.slug)
                && Objects.equals(this.status, that.status)
                && Objects.equals(this.suspendedAt, that.suspendedAt)
                && Objects.equals(this.suspendedBy, that.suspendedBy)
                && Objects.equals(this.suspensionReason, that.suspensionReason)
                && Objects.equals(this.provisioningState, that.provisioningState)
                && Objects.equals(this.zitadelOrganizationId, that.zitadelOrganizationId)
                && Objects.equals(this.zitadelDefaultUserId, that.zitadelDefaultUserId)
                && this.desiredGeneration == that.desiredGeneration
                && this.appliedGeneration == that.appliedGeneration
                && Objects.equals(this.lastSyncedAt, that.lastSyncedAt)
                && Objects.equals(this.nextReconcileAt, that.nextReconcileAt)
                && Objects.equals(this.lastReconcileAttemptAt, that.lastReconcileAttemptAt)
                && Objects.equals(this.lastReconcileTrigger, that.lastReconcileTrigger);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.slug, this.status, this.suspendedAt, this.suspendedBy, this.suspensionReason, this.provisioningState, this.zitadelOrganizationId, this.zitadelDefaultUserId, this.desiredGeneration, this.appliedGeneration, this.lastSyncedAt, this.nextReconcileAt, this.lastReconcileAttemptAt, this.lastReconcileTrigger);
    }

    @Override
    public String toString() {
        return "Tenant[" + "id=" + this.id + ", " + "slug=" + this.slug + ", " + "status=" + this.status + ", " + "suspendedAt=" + this.suspendedAt + ", " + "suspendedBy=" + this.suspendedBy + ", " + "suspensionReason=" + this.suspensionReason + ", " + "provisioningState=" + this.provisioningState + ", " + "zitadelOrganizationId=" + this.zitadelOrganizationId + ", " + "zitadelDefaultUserId=" + this.zitadelDefaultUserId + ", " + "desiredGeneration=" + this.desiredGeneration + ", " + "appliedGeneration=" + this.appliedGeneration + ", " + "lastSyncedAt=" + this.lastSyncedAt + ", " + "nextReconcileAt=" + this.nextReconcileAt + ", " + "lastReconcileAttemptAt=" + this.lastReconcileAttemptAt + ", " + "lastReconcileTrigger=" + this.lastReconcileTrigger + "]";
    }

    /**
     * Creates a new Tenant.
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
    @JsonCreator
    public Tenant(String id, String slug, TenantStatus status, String suspendedAt, String suspendedBy, String suspensionReason, TenantProvisioningState provisioningState, String zitadelOrganizationId, String zitadelDefaultUserId, long desiredGeneration, long appliedGeneration, String lastSyncedAt, String nextReconcileAt, String lastReconcileAttemptAt, String lastReconcileTrigger) {
        this.id = id;
        this.slug = slug;
        this.status = status;
        this.suspendedAt = suspendedAt;
        this.suspendedBy = suspendedBy;
        this.suspensionReason = suspensionReason;
        this.provisioningState = provisioningState;
        this.zitadelOrganizationId = zitadelOrganizationId;
        this.zitadelDefaultUserId = zitadelDefaultUserId;
        this.desiredGeneration = desiredGeneration;
        this.appliedGeneration = appliedGeneration;
        this.lastSyncedAt = lastSyncedAt;
        this.nextReconcileAt = nextReconcileAt;
        this.lastReconcileAttemptAt = lastReconcileAttemptAt;
        this.lastReconcileTrigger = lastReconcileTrigger;
    }
}
