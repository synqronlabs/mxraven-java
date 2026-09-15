package com.mxraven.admin.model;

/**
 * Request body for {@code PUT /v2/tenants/{slug}/listeners/{listener_id}}.
 * Only the display name is writable through this operation.
 */
public record UpdateListenerRequest(String displayName) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String displayName;

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public UpdateListenerRequest build() {
            return new UpdateListenerRequest(displayName);
        }
    }
}
