package com.mxraven.admin.model;

import java.util.List;

/**
 * Request body for {@code PUT /v2/tenants/{slug}/identity-provisioning}.
 */
public record UpdateIdentityProvisioningRequest(
        List<String> roles) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private List<String> roles;

        public Builder roles(List<String> roles) {
            this.roles = roles;
            return this;
        }

        public UpdateIdentityProvisioningRequest build() {
            List<String> value = roles == null ? List.of() : roles;
            RequestSupport.optionalList(value, "roles", 64);
            return new UpdateIdentityProvisioningRequest(value);
        }
    }
}
