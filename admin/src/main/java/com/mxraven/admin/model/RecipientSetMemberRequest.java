package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.regex.Pattern;

/**
 * Request body for <code>POST .../recipient-sets/{set_ref}/members</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class RecipientSetMemberRequest {
    private final String emailAddress;

    /** member email address */
    public String emailAddress() {
        return emailAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipientSetMemberRequest that = (RecipientSetMemberRequest) o;
        return Objects.equals(this.emailAddress, that.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.emailAddress);
    }

    @Override
    public String toString() {
        return "RecipientSetMemberRequest[" + "emailAddress=" + this.emailAddress + "]";
    }

    /**
     * Creates a new RecipientSetMemberRequest.
     *
     * @param emailAddress member email address
     */
    @JsonCreator
    public RecipientSetMemberRequest(String emailAddress) {
        this.emailAddress = emailAddress;
    }

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
            if (emailAddress == null || Java8.isBlank(emailAddress)) {
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
