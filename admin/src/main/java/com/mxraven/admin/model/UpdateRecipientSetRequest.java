package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request body for {@code PUT /v2/tenants/{slug}/recipient-sets/{set_ref}}.
 *
 * <p>Both fields are required; null clears the stored value.
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public record UpdateRecipientSetRequest(
        String displayName,
        String description) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String displayName;
        private String description;

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

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
