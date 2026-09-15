package com.mxraven.admin.model;

/**
 * Request body for
 * {@code PUT /v2/tenants/{slug}/identity-providers/{idp_id}/auto-grant}.
 */
public record UpdateIdentityProviderAutoGrantRequest(
        boolean enabled) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private boolean enabled;

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public UpdateIdentityProviderAutoGrantRequest build() {
            return new UpdateIdentityProviderAutoGrantRequest(enabled);
        }
    }
}
