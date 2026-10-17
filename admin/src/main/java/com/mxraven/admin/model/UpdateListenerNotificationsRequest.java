package com.mxraven.admin.model;

/**
 * Request body for
 * {@code PUT /v2/tenants/{slug}/listeners/{listener_id}/notifications}.
 *
 * @param enabled whether the listener publishes client notification events
 */
public record UpdateListenerNotificationsRequest(boolean enabled) {

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link UpdateListenerNotificationsRequest} instances. */
    public static final class Builder {
        private boolean enabled;

        /**
         * Sets whether the listener publishes client notification events.
         *
         * @param enabled whether client notifications are enabled
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
        public UpdateListenerNotificationsRequest build() {
            return new UpdateListenerNotificationsRequest(enabled);
        }
    }
}
