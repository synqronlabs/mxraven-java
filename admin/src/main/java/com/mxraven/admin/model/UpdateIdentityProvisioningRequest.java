package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * Request body for <code>PUT /v2/tenants/{slug}/identity-provisioning</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class UpdateIdentityProvisioningRequest {
    private final List<String> roles;

    /** roles to provision for the tenant */
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
        UpdateIdentityProvisioningRequest that = (UpdateIdentityProvisioningRequest) o;
        return Objects.equals(this.roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.roles);
    }

    @Override
    public String toString() {
        return "UpdateIdentityProvisioningRequest[" + "roles=" + this.roles + "]";
    }

    /**
     * Creates a new UpdateIdentityProvisioningRequest.
     *
     * @param roles roles to provision for the tenant
     */
    @JsonCreator
    public UpdateIdentityProvisioningRequest(List<String> roles) {
        this.roles = roles;
    }

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link UpdateIdentityProvisioningRequest} instances. */
    public static final class Builder {
        private List<String> roles;

        /**
         * Sets the roles to provision for the tenant.
         *
         * @param roles roles to provision
         * @return this builder
         */
        public Builder roles(List<String> roles) {
            this.roles = roles;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return the update request
         */
        public UpdateIdentityProvisioningRequest build() {
            List<String> value = roles == null ? Java8.list() : roles;
            RequestSupport.optionalList(value, "roles", 64);
            return new UpdateIdentityProvisioningRequest(value);
        }
    }
}
