package com.mxraven.admin.model;

/**
 * Request body for {@code PUT /v2/tenants/{slug}/webhook-endpoints/{endpoint_id}}.
 *
 * @param displayName human-readable endpoint name
 * @param targetUrl   HTTPS URL that deliveries are sent to
 */
public record UpdateWebhookEndpointRequest(
        String displayName,
        String targetUrl) {

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link UpdateWebhookEndpointRequest} instances. */
    public static final class Builder {
        private String displayName;
        private String targetUrl;

        /**
         * Sets the human-readable endpoint name.
         *
         * @param displayName human-readable endpoint name
         * @return this builder
         */
        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * Sets the HTTPS URL that deliveries are sent to.
         *
         * @param targetUrl HTTPS delivery target URL
         * @return this builder
         */
        public Builder targetUrl(String targetUrl) {
            this.targetUrl = targetUrl;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return the update request
         * @throws IllegalArgumentException if a field fails validation
         */
        public UpdateWebhookEndpointRequest build() {
            if (displayName == null || displayName.isBlank()) {
                throw new IllegalArgumentException("display_name is required");
            }
            if (displayName.codePointCount(0, displayName.length()) > 255) {
                throw new IllegalArgumentException("display_name must be 255 characters or fewer");
            }
            validateTargetUrl(targetUrl);
            return new UpdateWebhookEndpointRequest(displayName, targetUrl);
        }

        private static void validateTargetUrl(String targetUrl) {
            if (targetUrl == null || targetUrl.isBlank()) {
                throw new IllegalArgumentException("target_url is required");
            }
            String trimmed = targetUrl.trim();
            if (trimmed.length() > 2048) {
                throw new IllegalArgumentException("target_url must be 2048 characters or fewer");
            }
            if (!trimmed.startsWith("https://")) {
                throw new IllegalArgumentException("target_url must use https");
            }
            String host = trimmed.substring("https://".length());
            int slash = host.indexOf('/');
            if (slash >= 0) {
                host = host.substring(0, slash);
            }
            if (host.isBlank() || host.indexOf(' ') >= 0) {
                throw new IllegalArgumentException("target_url host is required");
            }
        }

    }
}
