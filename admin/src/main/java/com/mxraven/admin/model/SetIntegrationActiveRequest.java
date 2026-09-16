package com.mxraven.admin.model;

/**
 * Request body for replacing the active state of a secret-backed integration.
 *
 * @param isActive whether the integration is active
 */
public record SetIntegrationActiveRequest(boolean isActive) {

    /**
     * Creates a new builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link SetIntegrationActiveRequest} instances. */
    public static final class Builder {
        private boolean isActive;

        /**
         * Sets whether the integration is active.
         *
         * @param isActive whether the integration is active
         * @return this builder
         */
        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return a new request
         */
        public SetIntegrationActiveRequest build() {
            return new SetIntegrationActiveRequest(isActive);
        }
    }
}
