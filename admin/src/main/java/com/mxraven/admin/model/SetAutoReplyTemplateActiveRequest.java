package com.mxraven.admin.model;

/**
 * Request body for {@code PUT .../auto-reply-templates/{template_id}/active}.
 */
public record SetAutoReplyTemplateActiveRequest(
        boolean isActive) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private boolean isActive;

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public SetAutoReplyTemplateActiveRequest build() {
            return new SetAutoReplyTemplateActiveRequest(isActive);
        }
    }
}
