package com.mxraven.admin.model;

import java.util.regex.Pattern;

import java.util.List;

/**
 * Request body for the recipient-set member batch operations.
 */
public record RecipientSetMembersBatchRequest(
        List<String> emailAddresses) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private List<String> emailAddresses;

        public Builder emailAddresses(List<String> emailAddresses) {
            this.emailAddresses = emailAddresses;
            return this;
        }

        private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

        public RecipientSetMembersBatchRequest build() {
            if (emailAddresses == null || emailAddresses.isEmpty()) {
                throw new IllegalArgumentException("email_addresses must not be empty");
            }
            if (emailAddresses.size() > 1000) {
                throw new IllegalArgumentException("email_addresses must contain 1000 entries or fewer");
            }
            for (int index = 0; index < emailAddresses.size(); index++) {
                String emailAddress = emailAddresses.get(index);
                if (emailAddress == null || emailAddress.isBlank()) {
                    throw new IllegalArgumentException("email_addresses[" + index + "] is required");
                }
                if (emailAddress.length() > 255) {
                    throw new IllegalArgumentException("email_addresses[" + index
                            + "] must be 255 characters or fewer");
                }
                if (!EMAIL.matcher(emailAddress).matches()) {
                    throw new IllegalArgumentException("email_addresses[" + index
                            + "] must be a valid email address");
                }
            }
            return new RecipientSetMembersBatchRequest(emailAddresses);
        }
    }
}
