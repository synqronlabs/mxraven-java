package com.mxraven.admin.model;

import java.util.List;

/**
 * Request body for {@code PUT /v2/tenants/{slug}/identity-provisioning}.
 *
 * @param roles roles to provision for the tenant
 */
public record UpdateIdentityProvisioningRequest(
        List<String> roles) {

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
            List<String> value = roles == null ? List.of() : roles;
            RequestSupport.optionalList(value, "roles", 64);
            return new UpdateIdentityProvisioningRequest(value);
        }
    }
}
