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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String displayName;
        private String targetUrl;

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder targetUrl(String targetUrl) {
            this.targetUrl = targetUrl;
            return this;
        }

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
