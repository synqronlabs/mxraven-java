package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.regex.Pattern;

/**
 * Request body for <code>POST /v2/tenants/{slug}/webhook-endpoints</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CreateWebhookEndpointRequest {
    private final String webhookRef;
    private final String displayName;
    private final String targetUrl;

    /** immutable human-readable webhook reference */
    public String webhookRef() {
        return webhookRef;
    }

    /** human-readable endpoint name */
    public String displayName() {
        return displayName;
    }

    /** HTTPS URL that deliveries are sent to */
    public String targetUrl() {
        return targetUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateWebhookEndpointRequest that = (CreateWebhookEndpointRequest) o;
        return Objects.equals(this.webhookRef, that.webhookRef)
                && Objects.equals(this.displayName, that.displayName)
                && Objects.equals(this.targetUrl, that.targetUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.webhookRef, this.displayName, this.targetUrl);
    }

    @Override
    public String toString() {
        return "CreateWebhookEndpointRequest[" + "webhookRef=" + this.webhookRef + ", " + "displayName=" + this.displayName + ", " + "targetUrl=" + this.targetUrl + "]";
    }

    /**
     * Creates a new CreateWebhookEndpointRequest.
     *
     * @param webhookRef immutable human-readable webhook reference
     * @param displayName human-readable endpoint name
     * @param targetUrl HTTPS URL that deliveries are sent to
     */
    @JsonCreator
    public CreateWebhookEndpointRequest(String webhookRef, String displayName, String targetUrl) {
        this.webhookRef = webhookRef;
        this.displayName = displayName;
        this.targetUrl = targetUrl;
    }

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds a {@link CreateWebhookEndpointRequest}. */
    public static final class Builder {
        private String webhookRef;
        private String displayName;
        private String targetUrl;

        /**
         * Sets the immutable webhook reference.
         *
         * @param webhookRef webhook reference
         * @return this builder
         */
        public Builder webhookRef(String webhookRef) {
            this.webhookRef = webhookRef;
            return this;
        }

        /**
         * Sets the human-readable endpoint name.
         *
         * @param displayName endpoint name
         * @return this builder
         */
        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * Sets the HTTPS URL that deliveries are sent to.
         *
         * @param targetUrl target URL
         * @return this builder
         */
        public Builder targetUrl(String targetUrl) {
            this.targetUrl = targetUrl;
            return this;
        }

        private static final Pattern REFERENCE = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9._-]*$");

        /**
         * Builds the request.
         *
         * @return new request
         * @throws IllegalArgumentException when a field is missing or invalid
         */
        public CreateWebhookEndpointRequest build() {
            if (webhookRef == null || Java8.isBlank(webhookRef)) {
                throw new IllegalArgumentException("webhook_ref is required");
            }
            if (webhookRef.codePointCount(0, webhookRef.length()) > 100) {
                throw new IllegalArgumentException("webhook_ref must be 100 characters or fewer");
            }
            if (!REFERENCE.matcher(webhookRef).matches()) {
                throw new IllegalArgumentException("webhook_ref must start with a letter or digit and may contain "
                        + "letters, digits, dots, underscores, or hyphens");
            }
            if (displayName == null || Java8.isBlank(displayName)) {
                throw new IllegalArgumentException("display_name is required");
            }
            if (displayName.codePointCount(0, displayName.length()) > 255) {
                throw new IllegalArgumentException("display_name must be 255 characters or fewer");
            }
            validateTargetUrl(targetUrl);
            return new CreateWebhookEndpointRequest(webhookRef, displayName, targetUrl);
        }

        private static void validateTargetUrl(String targetUrl) {
            if (targetUrl == null || Java8.isBlank(targetUrl)) {
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
            if (Java8.isBlank(host) || host.indexOf(' ') >= 0) {
                throw new IllegalArgumentException("target_url host is required");
            }
        }

    }
}
