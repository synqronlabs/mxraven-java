package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for
 * {@code POST /v2/smtp-forward-destination-verifications/confirm}.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ConfirmSmtpForwardDestinationRequest {
    private final String token;

    /** destination-verification token */
    public String token() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfirmSmtpForwardDestinationRequest that = (ConfirmSmtpForwardDestinationRequest) o;
        return Objects.equals(this.token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.token);
    }

    @Override
    public String toString() {
        return "ConfirmSmtpForwardDestinationRequest[" + "token=" + this.token + "]";
    }

    /**
     * Creates a new ConfirmSmtpForwardDestinationRequest.
     *
     * @param token destination-verification token
     */
    @JsonCreator
    public ConfirmSmtpForwardDestinationRequest(String token) {
        this.token = token;
    }

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
            if (token == null || Java8.isBlank(token)) {
                throw new IllegalArgumentException("token is required");
            }
            if (token.length() < 40 || token.length() > 128) {
                throw new IllegalArgumentException("token must be between 40 and 128 characters");
            }
            return new ConfirmSmtpForwardDestinationRequest(token);
        }
    }
}
