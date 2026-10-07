package com.mxraven.admin.model;

/**
 * Request body for {@code POST /v2/tenants/{slug}/suppressions}.
 *
 * @param emailAddress email address to suppress
 * @param reason optional suppression reason; one of {@code unsubscribe} or
 *               {@code bounce}
 */
public record CreateTenantSuppressionRequest(
        String emailAddress,
        SuppressionReason reason) {

    /**
     * Validates and creates a suppression creation request.
     *
     * @param emailAddress email address to suppress
     * @param reason       optional suppression reason
     * @throws IllegalArgumentException if the email address is missing, longer
     *         than 255 characters, or not a valid mailbox
     */
    public CreateTenantSuppressionRequest {
        RequestSupport.requireString(emailAddress, "email_address", 255);
        if (emailAddress.indexOf('@') < 0 || emailAddress.indexOf('\r') >= 0 || emailAddress.indexOf('\n') >= 0) {
            throw new IllegalArgumentException("email_address must be a valid mailbox");
        }
    }

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds a {@link CreateTenantSuppressionRequest}. */
    public static final class Builder {
        private String emailAddress;
        private SuppressionReason reason;

        /**
         * Sets the email address to suppress.
         *
         * @param emailAddress email address
         * @return this builder
         */
        public Builder emailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        /**
         * Sets the suppression reason.
         *
         * @param reason suppression reason
         * @return this builder
         */
        public Builder reason(SuppressionReason reason) {
            this.reason = reason;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return new request
         * @throws IllegalArgumentException when a field is missing or invalid
         */
        public CreateTenantSuppressionRequest build() {
            return new CreateTenantSuppressionRequest(emailAddress, reason);
        }
    }
}
