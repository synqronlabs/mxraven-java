package com.mxraven.admin.model;

/**
 * Request body for
 * {@code PUT /v2/tenants/{slug}/identity-providers/{idp_id}/auto-grant}.
 *
 * @param enabled whether automatic role grants are enabled
 */
public record UpdateIdentityProviderAutoGrantRequest(
        boolean enabled) {

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link UpdateIdentityProviderAutoGrantRequest} instances. */
    public static final class Builder {
        private boolean enabled;

        /**
         * Sets whether automatic role grants are enabled.
         *
         * @param enabled whether automatic role grants are enabled
         * @return this builder
         */
        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return the update request
         */
        public UpdateIdentityProviderAutoGrantRequest build() {
            return new UpdateIdentityProviderAutoGrantRequest(enabled);
        }
    }
}
