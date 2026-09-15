package com.mxraven.admin.model;

import java.util.regex.Pattern;

/**
 * Request body for {@code POST /v2/tenants/{slug}/webhook-endpoints}.
 *
 * @param webhookRef  immutable human-readable webhook reference
 * @param displayName human-readable endpoint name
 * @param targetUrl   HTTPS URL that deliveries are sent to
 */
public record CreateWebhookEndpointRequest(
        String webhookRef,
        String displayName,
        String targetUrl) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String webhookRef;
        private String displayName;
        private String targetUrl;

        public Builder webhookRef(String webhookRef) {
            this.webhookRef = webhookRef;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder targetUrl(String targetUrl) {
            this.targetUrl = targetUrl;
            return this;
        }

        private static final Pattern REFERENCE = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9._-]*$");

        public CreateWebhookEndpointRequest build() {
            if (webhookRef == null || webhookRef.isBlank()) {
                throw new IllegalArgumentException("webhook_ref is required");
            }
            if (webhookRef.codePointCount(0, webhookRef.length()) > 100) {
                throw new IllegalArgumentException("webhook_ref must be 100 characters or fewer");
            }
            if (!REFERENCE.matcher(webhookRef).matches()) {
                throw new IllegalArgumentException("webhook_ref must start with a letter or digit and may contain "
                        + "letters, digits, dots, underscores, or hyphens");
            }
            if (displayName == null || displayName.isBlank()) {
                throw new IllegalArgumentException("display_name is required");
            }
            if (displayName.codePointCount(0, displayName.length()) > 255) {
                throw new IllegalArgumentException("display_name must be 255 characters or fewer");
            }
            validateTargetUrl(targetUrl);
            return new CreateWebhookEndpointRequest(webhookRef, displayName, targetUrl);
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
