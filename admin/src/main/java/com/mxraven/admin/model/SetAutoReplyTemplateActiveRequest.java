package com.mxraven.admin.model;

/**
 * Request body for {@code PUT .../auto-reply-templates/{template_id}/active}.
 *
 * @param isActive whether the template is active
 */
public record SetAutoReplyTemplateActiveRequest(
        boolean isActive) {

    /**
     * Creates a new builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link SetAutoReplyTemplateActiveRequest} instances. */
    public static final class Builder {
        private boolean isActive;

        /**
         * Sets whether the template is active.
         *
         * @param isActive whether the template is active
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
        public SetAutoReplyTemplateActiveRequest build() {
            return new SetAutoReplyTemplateActiveRequest(isActive);
        }
    }
}
