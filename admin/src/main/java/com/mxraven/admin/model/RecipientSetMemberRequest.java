package com.mxraven.admin.model;

import java.util.regex.Pattern;

/**
 * Request body for {@code POST .../recipient-sets/{set_ref}/members}.
 *
 * @param emailAddress member email address
 */
public record RecipientSetMemberRequest(
        String emailAddress) {

    /**
     * Creates a new builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds a {@link RecipientSetMemberRequest}. */
    public static final class Builder {
        private String emailAddress;

        /**
         * Sets the member email address.
         *
         * @param emailAddress member email address
         * @return this builder
         */
        public Builder emailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

        /**
         * Builds the request.
         *
         * @return a new request
         * @throws IllegalArgumentException if the email address is missing, too long, or invalid
         */
        public RecipientSetMemberRequest build() {
            if (emailAddress == null || emailAddress.isBlank()) {
                throw new IllegalArgumentException("email_address is required");
            }
            if (emailAddress.length() > 255) {
                throw new IllegalArgumentException("email_address must be 255 characters or fewer");
            }
            if (!EMAIL.matcher(emailAddress).matches()) {
                throw new IllegalArgumentException("email_address must be a valid email address");
            }
            return new RecipientSetMemberRequest(emailAddress);
        }
    }
}
