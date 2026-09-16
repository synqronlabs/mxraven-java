package com.mxraven.admin.model;

import java.util.regex.Pattern;

/**
 * Request body for {@code POST /v2/tenants/{slug}/recipient-sets}.
 *
 * @param setRef immutable recipient-set reference
 * @param displayName optional human-readable set name
 * @param description optional set description
 */
public record CreateRecipientSetRequest(
        String setRef,
        String displayName,
        String description) {

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds a {@link CreateRecipientSetRequest}. */
    public static final class Builder {
        private String setRef;
        private String displayName;
        private String description;

        /**
         * Sets the immutable recipient-set reference.
         *
         * @param setRef recipient-set reference
         * @return this builder
         */
        public Builder setRef(String setRef) {
            this.setRef = setRef;
            return this;
        }

        /**
         * Sets the human-readable set name.
         *
         * @param displayName set name
         * @return this builder
         */
        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * Sets the set description.
         *
         * @param description set description
         * @return this builder
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        private static final Pattern REFERENCE = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9._-]*$");

        /**
         * Builds the request.
         *
         * @return new request
         * @throws IllegalArgumentException when a field is missing or invalid
         */
        public CreateRecipientSetRequest build() {
            if (setRef == null || setRef.isBlank()) {
                throw new IllegalArgumentException("set_ref is required");
            }
            if (setRef.codePointCount(0, setRef.length()) > 100) {
                throw new IllegalArgumentException("set_ref must be 100 characters or fewer");
            }
            if (!REFERENCE.matcher(setRef).matches()) {
                throw new IllegalArgumentException("set_ref must start with a letter or digit and may contain "
                        + "letters, digits, dots, underscores, or hyphens");
            }
            if (displayName != null && displayName.codePointCount(0, displayName.length()) > 255) {
                throw new IllegalArgumentException("display_name must be 255 characters or fewer");
            }
            if (description != null && description.codePointCount(0, description.length()) > 4096) {
                throw new IllegalArgumentException("description must be 4096 characters or fewer");
            }
            return new CreateRecipientSetRequest(setRef, displayName, description);
        }
    }
}
