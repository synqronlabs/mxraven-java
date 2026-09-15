package com.mxraven.admin.model;

import java.util.regex.Pattern;

/**
 * Request body for {@code POST /v2/tenants/{slug}/recipient-sets}.
 */
public record CreateRecipientSetRequest(
        String setRef,
        String displayName,
        String description) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String setRef;
        private String displayName;
        private String description;

        public Builder setRef(String setRef) {
            this.setRef = setRef;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        private static final Pattern REFERENCE = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9._-]*$");

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
