package com.mxraven.admin.model;

/**
 * Request body for
 * {@code PUT /v2/tenants/{slug}/listeners/{listener_id}/rspamd-scanning}.
 *
 * @param enabled whether inbound Rspamd scanning is enabled
 */
public record UpdateListenerRspamdScanningRequest(boolean enabled) {

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link UpdateListenerRspamdScanningRequest} instances. */
    public static final class Builder {
        private boolean enabled;

        /**
         * Sets whether inbound Rspamd scanning is enabled.
         *
         * @param enabled whether inbound scanning is enabled
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
        public UpdateListenerRspamdScanningRequest build() {
            return new UpdateListenerRspamdScanningRequest(enabled);
        }
    }
}
