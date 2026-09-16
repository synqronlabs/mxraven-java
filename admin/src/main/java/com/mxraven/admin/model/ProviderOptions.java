package com.mxraven.admin.model;

/**
 * Provider-level options passed through to ZITADEL.
 *
 * @param isLinkingAllowed  whether linking to an existing account is allowed
 * @param isCreationAllowed whether creating a new account is allowed
 * @param isAutoCreation    whether an account is created automatically
 * @param isAutoUpdate      whether a linked account is updated automatically
 * @param autoLinking       automatic linking behavior
 */
public record ProviderOptions(
        Boolean isLinkingAllowed,
        Boolean isCreationAllowed,
        Boolean isAutoCreation,
        Boolean isAutoUpdate,
        IdpAutoLinking autoLinking) {

    /**
     * Creates a new builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds a {@link ProviderOptions}. */
    public static final class Builder {
        private Boolean isLinkingAllowed;
        private Boolean isCreationAllowed;
        private Boolean isAutoCreation;
        private Boolean isAutoUpdate;
        private IdpAutoLinking autoLinking;

        /**
         * Sets whether linking to an existing account is allowed.
         *
         * @param isLinkingAllowed whether linking is allowed
         * @return this builder
         */
        public Builder isLinkingAllowed(Boolean isLinkingAllowed) {
            this.isLinkingAllowed = isLinkingAllowed;
            return this;
        }

        /**
         * Sets whether creating a new account is allowed.
         *
         * @param isCreationAllowed whether creation is allowed
         * @return this builder
         */
        public Builder isCreationAllowed(Boolean isCreationAllowed) {
            this.isCreationAllowed = isCreationAllowed;
            return this;
        }

        /**
         * Sets whether an account is created automatically.
         *
         * @param isAutoCreation whether auto-creation is enabled
         * @return this builder
         */
        public Builder isAutoCreation(Boolean isAutoCreation) {
            this.isAutoCreation = isAutoCreation;
            return this;
        }

        /**
         * Sets whether a linked account is updated automatically.
         *
         * @param isAutoUpdate whether auto-update is enabled
         * @return this builder
         */
        public Builder isAutoUpdate(Boolean isAutoUpdate) {
            this.isAutoUpdate = isAutoUpdate;
            return this;
        }

        /**
         * Sets the automatic linking behavior.
         *
         * @param autoLinking automatic linking behavior
         * @return this builder
         */
        public Builder autoLinking(IdpAutoLinking autoLinking) {
            this.autoLinking = autoLinking;
            return this;
        }

        /**
         * Builds the provider options.
         *
         * @return a new provider options instance
         */
        public ProviderOptions build() {
            return new ProviderOptions(isLinkingAllowed, isCreationAllowed, isAutoCreation, isAutoUpdate, autoLinking);
        }
    }
}
