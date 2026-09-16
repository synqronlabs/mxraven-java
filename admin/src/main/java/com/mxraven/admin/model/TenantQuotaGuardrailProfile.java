package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * The guardrail profile summary embedded in a {@link TenantQuota}.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class TenantQuotaGuardrailProfile {
    private final String id;
    private final String profileRef;
    private final String displayName;
    private final boolean isActive;

    /** unique profile identifier */
    public String id() {
        return id;
    }

    /** stable reference for the profile */
    public String profileRef() {
        return profileRef;
    }

    /** human-readable profile name */
    public String displayName() {
        return displayName;
    }

    /** whether the profile is active */
    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TenantQuotaGuardrailProfile that = (TenantQuotaGuardrailProfile) o;
        return Objects.equals(this.id, that.id)
                && Objects.equals(this.profileRef, that.profileRef)
                && Objects.equals(this.displayName, that.displayName)
                && this.isActive == that.isActive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.profileRef, this.displayName, this.isActive);
    }

    @Override
    public String toString() {
        return "TenantQuotaGuardrailProfile[" + "id=" + this.id + ", " + "profileRef=" + this.profileRef + ", " + "displayName=" + this.displayName + ", " + "isActive=" + this.isActive + "]";
    }

    /**
     * Creates a new TenantQuotaGuardrailProfile.
     *
     * @param id unique profile identifier
     * @param profileRef stable reference for the profile
     * @param displayName human-readable profile name
     * @param isActive whether the profile is active
     */
    @JsonCreator
    public TenantQuotaGuardrailProfile(String id, String profileRef, String displayName, boolean isActive) {
        this.id = id;
        this.profileRef = profileRef;
        this.displayName = displayName;
        this.isActive = isActive;
    }
}
