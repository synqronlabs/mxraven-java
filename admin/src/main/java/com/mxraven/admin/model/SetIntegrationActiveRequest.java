package com.mxraven.admin.model;

/**
 * Request body for replacing the active state of a secret-backed integration.
 */
public record SetIntegrationActiveRequest(boolean isActive) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private boolean isActive;

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public SetIntegrationActiveRequest build() {
            return new SetIntegrationActiveRequest(isActive);
        }
    }
}
