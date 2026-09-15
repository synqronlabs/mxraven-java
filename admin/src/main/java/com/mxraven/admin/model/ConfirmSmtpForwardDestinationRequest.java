package com.mxraven.admin.model;

/**
 * Request body for
 * {@code POST /v2/smtp-forward-destination-verifications/confirm}.
 */
public record ConfirmSmtpForwardDestinationRequest(
        String token) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String token;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

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
