package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request body for {@code PUT /v2/tenants/{slug}/recipient-sets/{set_ref}}.
 *
 * <p>Both fields are required; null clears the stored value.
 *
 * @param displayName human-readable recipient set name
 * @param description recipient set description
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public record UpdateRecipientSetRequest(
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

    /** Builds {@link UpdateRecipientSetRequest} instances. */
    public static final class Builder {
        private String displayName;
        private String description;

        /**
         * Sets the human-readable recipient set name.
         *
         * @param displayName human-readable recipient set name
         * @return this builder
         */
        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * Sets the recipient set description.
         *
         * @param description recipient set description
         * @return this builder
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return the update request
         * @throws IllegalArgumentException if a field exceeds its length limit
         */
        public UpdateRecipientSetRequest build() {
            if (displayName != null && displayName.codePointCount(0, displayName.length()) > 255) {
                throw new IllegalArgumentException("display_name must be 255 characters or fewer");
            }
            if (description != null && description.codePointCount(0, description.length()) > 4096) {
                throw new IllegalArgumentException("description must be 4096 characters or fewer");
            }
            return new UpdateRecipientSetRequest(displayName, description);
        }
    }
}
