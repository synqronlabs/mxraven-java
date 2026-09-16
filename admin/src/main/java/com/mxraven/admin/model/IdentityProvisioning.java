package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * Tenant identity-provider provisioning settings.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class IdentityProvisioning {
    private final List<String> roles;

    /** roles granted to provisioned identities */
    public List<String> roles() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdentityProvisioning that = (IdentityProvisioning) o;
        return Objects.equals(this.roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.roles);
    }

    @Override
    public String toString() {
        return "IdentityProvisioning[" + "roles=" + this.roles + "]";
    }

    /**
     * Creates a new IdentityProvisioning.
     *
     * @param roles roles granted to provisioned identities
     */
    @JsonCreator
    public IdentityProvisioning(List<String> roles) {
        this.roles = roles;
    }
}
