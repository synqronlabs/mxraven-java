package com.mxraven.admin.model;

/**
 * Request body for {@code PUT /v2/tenants/{slug}/listeners/{listener_id}}.
 * Only the display name is writable through this operation.
 *
 * @param displayName new human-readable listener name
 */
public record UpdateListenerRequest(String displayName) {

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link UpdateListenerRequest} instances. */
    public static final class Builder {
        private String displayName;

        /**
         * Sets the new human-readable listener name.
         *
         * @param displayName new human-readable listener name
         * @return this builder
         */
        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return the update request
         */
        public UpdateListenerRequest build() {
            return new UpdateListenerRequest(displayName);
        }
    }
}
