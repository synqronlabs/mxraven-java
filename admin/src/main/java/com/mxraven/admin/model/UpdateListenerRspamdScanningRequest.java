package com.mxraven.admin.model;

/**
 * Request body for
 * {@code PUT /v2/tenants/{slug}/listeners/{listener_id}/rspamd-scanning}.
 */
public record UpdateListenerRspamdScanningRequest(boolean enabled) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private boolean enabled;

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public UpdateListenerRspamdScanningRequest build() {
            return new UpdateListenerRspamdScanningRequest(enabled);
        }
    }
}
