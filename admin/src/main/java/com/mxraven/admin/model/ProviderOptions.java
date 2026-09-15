package com.mxraven.admin.model;

/**
 * Provider-level options passed through to ZITADEL.
 */
public record ProviderOptions(
        Boolean isLinkingAllowed,
        Boolean isCreationAllowed,
        Boolean isAutoCreation,
        Boolean isAutoUpdate,
        IdpAutoLinking autoLinking) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Boolean isLinkingAllowed;
        private Boolean isCreationAllowed;
        private Boolean isAutoCreation;
        private Boolean isAutoUpdate;
        private IdpAutoLinking autoLinking;

        public Builder isLinkingAllowed(Boolean isLinkingAllowed) {
            this.isLinkingAllowed = isLinkingAllowed;
            return this;
        }

        public Builder isCreationAllowed(Boolean isCreationAllowed) {
            this.isCreationAllowed = isCreationAllowed;
            return this;
        }

        public Builder isAutoCreation(Boolean isAutoCreation) {
            this.isAutoCreation = isAutoCreation;
            return this;
        }

        public Builder isAutoUpdate(Boolean isAutoUpdate) {
            this.isAutoUpdate = isAutoUpdate;
            return this;
        }

        public Builder autoLinking(IdpAutoLinking autoLinking) {
            this.autoLinking = autoLinking;
            return this;
        }

        public ProviderOptions build() {
            return new ProviderOptions(isLinkingAllowed, isCreationAllowed, isAutoCreation, isAutoUpdate, autoLinking);
        }
    }
}
