package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for <code>PUT /v2/tenants/{slug}/webhook-endpoints/{endpoint_id}</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class UpdateWebhookEndpointRequest {
    private final String displayName;
    private final String targetUrl;

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
        UpdateWebhookEndpointRequest that = (UpdateWebhookEndpointRequest) o;
        return Objects.equals(this.displayName, that.displayName)
                && Objects.equals(this.targetUrl, that.targetUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.displayName, this.targetUrl);
    }

    @Override
    public String toString() {
        return "UpdateWebhookEndpointRequest[" + "displayName=" + this.displayName + ", " + "targetUrl=" + this.targetUrl + "]";
    }

    /**
     * Creates a new UpdateWebhookEndpointRequest.
     *
     * @param displayName human-readable endpoint name
     * @param targetUrl HTTPS URL that deliveries are sent to
     */
    @JsonCreator
    public UpdateWebhookEndpointRequest(String displayName, String targetUrl) {
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
            if (displayName == null || Java8.isBlank(displayName)) {
                throw new IllegalArgumentException("display_name is required");
            }
            if (displayName.codePointCount(0, displayName.length()) > 255) {
                throw new IllegalArgumentException("display_name must be 255 characters or fewer");
            }
            validateTargetUrl(targetUrl);
            return new UpdateWebhookEndpointRequest(displayName, targetUrl);
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
