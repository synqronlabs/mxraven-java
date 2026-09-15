package com.mxraven.admin.model;

import java.util.regex.Pattern;

/**
 * Request body for {@code POST .../recipient-sets/{set_ref}/members}.
 */
public record RecipientSetMemberRequest(
        String emailAddress) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String emailAddress;

        public Builder emailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

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
