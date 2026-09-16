package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Public login context for a tenant.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class TenantLoginContext {
    private final String tenantSlug;
    private final String workspaceRef;
    private final String displayName;
    private final String zitadelOrganizationId;

    /** slug of the tenant */
    public String tenantSlug() {
        return tenantSlug;
    }

    /** reference of the tenant workspace */
    public String workspaceRef() {
        return workspaceRef;
    }

    /** human-readable tenant name */
    public String displayName() {
        return displayName;
    }

    /** Zitadel organization identifier */
    public String zitadelOrganizationId() {
        return zitadelOrganizationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TenantLoginContext that = (TenantLoginContext) o;
        return Objects.equals(this.tenantSlug, that.tenantSlug)
                && Objects.equals(this.workspaceRef, that.workspaceRef)
                && Objects.equals(this.displayName, that.displayName)
                && Objects.equals(this.zitadelOrganizationId, that.zitadelOrganizationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.tenantSlug, this.workspaceRef, this.displayName, this.zitadelOrganizationId);
    }

    @Override
    public String toString() {
        return "TenantLoginContext[" + "tenantSlug=" + this.tenantSlug + ", " + "workspaceRef=" + this.workspaceRef + ", " + "displayName=" + this.displayName + ", " + "zitadelOrganizationId=" + this.zitadelOrganizationId + "]";
    }

    /**
     * Creates a new TenantLoginContext.
     *
     * @param tenantSlug slug of the tenant
     * @param workspaceRef reference of the tenant workspace
     * @param displayName human-readable tenant name
     * @param zitadelOrganizationId Zitadel organization identifier
     */
    @JsonCreator
    public TenantLoginContext(String tenantSlug, String workspaceRef, String displayName, String zitadelOrganizationId) {
        this.tenantSlug = tenantSlug;
        this.workspaceRef = workspaceRef;
        this.displayName = displayName;
        this.zitadelOrganizationId = zitadelOrganizationId;
    }
}
