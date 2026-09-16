package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for <code>PUT /v2/tenants/{slug}/storage-integrations/{integration_id}</code>.
 * This is a full replacement of the integration configuration and credentials.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class UpdateStorageIntegrationRequest {
    private final String displayName;
    private final String accessKey;
    private final String secretKey;
    private final String bucketName;
    private final String region;
    private final String endpointUrl;
    private final boolean forcePathStyle;

    /** human-readable integration name */
    public String displayName() {
        return displayName;
    }

    /** storage access key */
    public String accessKey() {
        return accessKey;
    }

    /** storage secret key */
    public String secretKey() {
        return secretKey;
    }

    /** target bucket name */
    public String bucketName() {
        return bucketName;
    }

    /** bucket region */
    public String region() {
        return region;
    }

    /** custom S3-compatible endpoint; may be empty for AWS */
    public String endpointUrl() {
        return endpointUrl;
    }

    /** use path-style bucket addressing */
    public boolean forcePathStyle() {
        return forcePathStyle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpdateStorageIntegrationRequest that = (UpdateStorageIntegrationRequest) o;
        return Objects.equals(this.displayName, that.displayName)
                && Objects.equals(this.accessKey, that.accessKey)
                && Objects.equals(this.secretKey, that.secretKey)
                && Objects.equals(this.bucketName, that.bucketName)
                && Objects.equals(this.region, that.region)
                && Objects.equals(this.endpointUrl, that.endpointUrl)
                && this.forcePathStyle == that.forcePathStyle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.displayName, this.accessKey, this.secretKey, this.bucketName, this.region, this.endpointUrl, this.forcePathStyle);
    }

    @Override
    public String toString() {
        return "UpdateStorageIntegrationRequest[" + "displayName=" + this.displayName + ", " + "accessKey=" + this.accessKey + ", " + "secretKey=" + this.secretKey + ", " + "bucketName=" + this.bucketName + ", " + "region=" + this.region + ", " + "endpointUrl=" + this.endpointUrl + ", " + "forcePathStyle=" + this.forcePathStyle + "]";
    }

    /**
     * Creates a new UpdateStorageIntegrationRequest.
     *
     * @param displayName human-readable integration name
     * @param accessKey storage access key
     * @param secretKey storage secret key
     * @param bucketName target bucket name
     * @param region bucket region
     * @param endpointUrl custom S3-compatible endpoint; may be empty for AWS
     * @param forcePathStyle use path-style bucket addressing
     */
    @JsonCreator
    public UpdateStorageIntegrationRequest(String displayName, String accessKey, String secretKey, String bucketName, String region, String endpointUrl, boolean forcePathStyle) {
        this.displayName = displayName;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucketName = bucketName;
        this.region = region;
        this.endpointUrl = endpointUrl;
        this.forcePathStyle = forcePathStyle;
    }

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link UpdateStorageIntegrationRequest} instances. */
    public static final class Builder {
        private String displayName;
        private String accessKey;
        private String secretKey;
        private String bucketName;
        private String region;
        private String endpointUrl;
        private boolean forcePathStyle;

        /**
         * Sets the human-readable integration name.
         *
         * @param displayName human-readable integration name
         * @return this builder
         */
        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * Sets the storage access key.
         *
         * @param accessKey storage access key
         * @return this builder
         */
        public Builder accessKey(String accessKey) {
            this.accessKey = accessKey;
            return this;
        }

        /**
         * Sets the storage secret key.
         *
         * @param secretKey storage secret key
         * @return this builder
         */
        public Builder secretKey(String secretKey) {
            this.secretKey = secretKey;
            return this;
        }

        /**
         * Sets the target bucket name.
         *
         * @param bucketName target bucket name
         * @return this builder
         */
        public Builder bucketName(String bucketName) {
            this.bucketName = bucketName;
            return this;
        }

        /**
         * Sets the bucket region.
         *
         * @param region bucket region
         * @return this builder
         */
        public Builder region(String region) {
            this.region = region;
            return this;
        }

        /**
         * Sets the custom S3-compatible endpoint; use an empty string for AWS.
         *
         * @param endpointUrl endpoint URL; may be empty for AWS
         * @return this builder
         */
        public Builder endpointUrl(String endpointUrl) {
            this.endpointUrl = endpointUrl;
            return this;
        }

        /**
         * Sets whether path-style bucket addressing is used.
         *
         * @param forcePathStyle whether path-style addressing is used
         * @return this builder
         */
        public Builder forcePathStyle(boolean forcePathStyle) {
            this.forcePathStyle = forcePathStyle;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return the update request
         * @throws IllegalArgumentException if a field fails validation
         */
        public UpdateStorageIntegrationRequest build() {
            validateCommon(displayName, accessKey, secretKey, bucketName, region, endpointUrl);
            return new UpdateStorageIntegrationRequest(displayName, accessKey, secretKey, bucketName, region, endpointUrl, forcePathStyle);
        }

        private static void validateCommon(String displayName, String accessKey, String secretKey,
                                           String bucketName, String region, String endpointUrl) {
            require(displayName, "display_name", 255);
            require(accessKey, "access_key", 4096);
            require(secretKey, "secret_key", 4096);
            require(bucketName, "bucket_name", 255);
            require(region, "region", 255);
            if (endpointUrl == null) {
                throw new IllegalArgumentException("endpoint_url is required; use an empty string for AWS");
            }
            String trimmedEndpoint = endpointUrl.trim();
            if (trimmedEndpoint.length() > 2048) {
                throw new IllegalArgumentException("endpoint_url must be 2048 characters or fewer");
            }
            if (!trimmedEndpoint.isEmpty()) {
                if (!trimmedEndpoint.startsWith("http://") && !trimmedEndpoint.startsWith("https://")) {
                    throw new IllegalArgumentException("endpoint_url must use http or https");
                }
                if (trimmedEndpoint.indexOf('?') >= 0 || trimmedEndpoint.indexOf('#') >= 0) {
                    throw new IllegalArgumentException("endpoint_url must not include a query or fragment");
                }
            }
        }

        private static void require(String value, String field, int maxLength) {
            if (value == null || Java8.isBlank(value)) {
                throw new IllegalArgumentException(field + " is required");
            }
            if (value.codePointCount(0, value.length()) > maxLength) {
                throw new IllegalArgumentException(field + " must be " + maxLength + " characters or fewer");
            }
        }

    }
}
