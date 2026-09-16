package com.mxraven.admin.model;

/**
 * Request body for
 * {@code POST /v2/smtp-forward-destination-verifications/confirm}.
 *
 * @param token destination-verification token
 */
public record ConfirmSmtpForwardDestinationRequest(
        String token) {

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds a {@link ConfirmSmtpForwardDestinationRequest}. */
    public static final class Builder {
        private String token;

        /**
         * Sets the destination-verification token.
         *
         * @param token verification token
         * @return this builder
         */
        public Builder token(String token) {
            this.token = token;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return new request
         */
        public ConfirmSmtpForwardDestinationRequest build() {
            if (token == null || token.isBlank()) {
                throw new IllegalArgumentException("token is required");
            }
            if (token.length() < 40 || token.length() > 128) {
                throw new IllegalArgumentException("token must be between 40 and 128 characters");
            }
            return new ConfirmSmtpForwardDestinationRequest(token);
        }
    }
}
