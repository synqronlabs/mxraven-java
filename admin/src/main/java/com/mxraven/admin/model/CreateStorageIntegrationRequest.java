package com.mxraven.admin.model;

import java.util.regex.Pattern;

/**
 * Request body for {@code POST /v2/tenants/{slug}/storage-integrations}.
 *
 * @param storageRef    immutable human-readable storage reference
 * @param displayName   human-readable integration name
 * @param accessKey     storage access key
 * @param secretKey     storage secret key
 * @param bucketName    target bucket name
 * @param region        bucket region
 * @param endpointUrl   custom S3-compatible endpoint; may be empty for AWS
 * @param forcePathStyle use path-style bucket addressing
 */
public record CreateStorageIntegrationRequest(
        String storageRef,
        String displayName,
        String accessKey,
        String secretKey,
        String bucketName,
        String region,
        String endpointUrl,
        boolean forcePathStyle) {

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds a {@link CreateStorageIntegrationRequest}. */
    public static final class Builder {
        private String storageRef;
        private String displayName;
        private String accessKey;
        private String secretKey;
        private String bucketName;
        private String region;
        private String endpointUrl;
        private boolean forcePathStyle;

        /**
         * Sets the immutable storage reference.
         *
         * @param storageRef storage reference
         * @return this builder
         */
        public Builder storageRef(String storageRef) {
            this.storageRef = storageRef;
            return this;
        }

        /**
         * Sets the human-readable integration name.
         *
         * @param displayName integration name
         * @return this builder
         */
        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * Sets the storage access key.
         *
         * @param accessKey access key
         * @return this builder
         */
        public Builder accessKey(String accessKey) {
            this.accessKey = accessKey;
            return this;
        }

        /**
         * Sets the storage secret key.
         *
         * @param secretKey secret key
         * @return this builder
         */
        public Builder secretKey(String secretKey) {
            this.secretKey = secretKey;
            return this;
        }

        /**
         * Sets the target bucket name.
         *
         * @param bucketName bucket name
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
         * Sets the custom S3-compatible endpoint.
         *
         * @param endpointUrl endpoint URL; empty for AWS
         * @return this builder
         */
        public Builder endpointUrl(String endpointUrl) {
            this.endpointUrl = endpointUrl;
            return this;
        }

        /**
         * Sets whether to use path-style bucket addressing.
         *
         * @param forcePathStyle path-style flag
         * @return this builder
         */
        public Builder forcePathStyle(boolean forcePathStyle) {
            this.forcePathStyle = forcePathStyle;
            return this;
        }

        private static final Pattern REFERENCE = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9._-]*$");

        /**
         * Builds the request.
         *
         * @return new request
         * @throws IllegalArgumentException when a field is missing or invalid
         */
        public CreateStorageIntegrationRequest build() {
            if (storageRef == null || storageRef.isBlank()) {
                throw new IllegalArgumentException("storage_ref is required");
            }
            if (storageRef.codePointCount(0, storageRef.length()) > 100) {
                throw new IllegalArgumentException("storage_ref must be 100 characters or fewer");
            }
            if (!REFERENCE.matcher(storageRef).matches()) {
                throw new IllegalArgumentException("storage_ref must start with a letter or digit and may contain "
                        + "letters, digits, dots, underscores, or hyphens");
            }
            validateCommon(displayName, accessKey, secretKey, bucketName, region, endpointUrl);
            return new CreateStorageIntegrationRequest(storageRef, displayName, accessKey, secretKey, bucketName, region, endpointUrl, forcePathStyle);
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
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException(field + " is required");
            }
            if (value.codePointCount(0, value.length()) > maxLength) {
                throw new IllegalArgumentException(field + " must be " + maxLength + " characters or fewer");
            }
        }

    }
}
