package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.regex.Pattern;

import java.util.List;

/**
 * Request body for the recipient-set member batch operations.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class RecipientSetMembersBatchRequest {
    private final List<String> emailAddresses;

    /** member email addresses */
    public List<String> emailAddresses() {
        return emailAddresses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipientSetMembersBatchRequest that = (RecipientSetMembersBatchRequest) o;
        return Objects.equals(this.emailAddresses, that.emailAddresses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.emailAddresses);
    }

    @Override
    public String toString() {
        return "RecipientSetMembersBatchRequest[" + "emailAddresses=" + this.emailAddresses + "]";
    }

    /**
     * Creates a new RecipientSetMembersBatchRequest.
     *
     * @param emailAddresses member email addresses
     */
    @JsonCreator
    public RecipientSetMembersBatchRequest(List<String> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    /**
     * Creates a new builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds a {@link RecipientSetMembersBatchRequest}. */
    public static final class Builder {
        private List<String> emailAddresses;

        /**
         * Sets the member email addresses.
         *
         * @param emailAddresses member email addresses
         * @return this builder
         */
        public Builder emailAddresses(List<String> emailAddresses) {
            this.emailAddresses = emailAddresses;
            return this;
        }

        private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

        /**
         * Builds the request.
         *
         * @return a new request
         * @throws IllegalArgumentException if the addresses are empty, too many, or invalid
         */
        public RecipientSetMembersBatchRequest build() {
            if (emailAddresses == null || emailAddresses.isEmpty()) {
                throw new IllegalArgumentException("email_addresses must not be empty");
            }
            if (emailAddresses.size() > 1000) {
                throw new IllegalArgumentException("email_addresses must contain 1000 entries or fewer");
            }
            for (int index = 0; index < emailAddresses.size(); index++) {
                String emailAddress = emailAddresses.get(index);
                if (emailAddress == null || Java8.isBlank(emailAddress)) {
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
