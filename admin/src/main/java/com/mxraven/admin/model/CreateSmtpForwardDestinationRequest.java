package com.mxraven.admin.model;

import java.util.regex.Pattern;

/**
 * Request body for
 * {@code POST /v2/tenants/{slug}/smtp-forward-destinations}.
 */
public record CreateSmtpForwardDestinationRequest(
        String destinationRef,
        String displayName,
        String emailAddress) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String destinationRef;
        private String displayName;
        private String emailAddress;

        public Builder destinationRef(String destinationRef) {
            this.destinationRef = destinationRef;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder emailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        private static final Pattern REFERENCE = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9._-]*$");

        public CreateSmtpForwardDestinationRequest build() {
            if (destinationRef == null || destinationRef.isBlank()) {
                throw new IllegalArgumentException("destination_ref is required");
            }
            if (destinationRef.codePointCount(0, destinationRef.length()) > 100) {
                throw new IllegalArgumentException("destination_ref must be 100 characters or fewer");
            }
            if (!REFERENCE.matcher(destinationRef).matches()) {
                throw new IllegalArgumentException("destination_ref must start with a letter or digit and may contain "
                        + "letters, digits, dots, underscores, or hyphens");
            }
            if (displayName == null || displayName.isBlank()) {
                throw new IllegalArgumentException("display_name is required");
            }
            if (displayName.codePointCount(0, displayName.length()) > 255) {
                throw new IllegalArgumentException("display_name must be 255 characters or fewer");
            }
            if (emailAddress == null || emailAddress.isBlank()) {
                throw new IllegalArgumentException("email_address is required");
            }
            if (emailAddress.codePointCount(0, emailAddress.length()) > 320) {
                throw new IllegalArgumentException("email_address must be 320 characters or fewer");
            }
            if (emailAddress.indexOf('@') < 0 || emailAddress.indexOf('\r') >= 0 || emailAddress.indexOf('\n') >= 0) {
                throw new IllegalArgumentException("email_address must be a valid mailbox");
            }
            return new CreateSmtpForwardDestinationRequest(destinationRef, displayName, emailAddress);
        }
    }
}
