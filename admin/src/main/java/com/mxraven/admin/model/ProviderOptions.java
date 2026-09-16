package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Provider-level options passed through to ZITADEL.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ProviderOptions {
    private final Boolean isLinkingAllowed;
    private final Boolean isCreationAllowed;
    private final Boolean isAutoCreation;
    private final Boolean isAutoUpdate;
    private final IdpAutoLinking autoLinking;

    /** whether linking to an existing account is allowed */
    public Boolean isLinkingAllowed() {
        return isLinkingAllowed;
    }

    /** whether creating a new account is allowed */
    public Boolean isCreationAllowed() {
        return isCreationAllowed;
    }

    /** whether an account is created automatically */
    public Boolean isAutoCreation() {
        return isAutoCreation;
    }

    /** whether a linked account is updated automatically */
    public Boolean isAutoUpdate() {
        return isAutoUpdate;
    }

    /** automatic linking behavior */
    public IdpAutoLinking autoLinking() {
        return autoLinking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProviderOptions that = (ProviderOptions) o;
        return Objects.equals(this.isLinkingAllowed, that.isLinkingAllowed)
                && Objects.equals(this.isCreationAllowed, that.isCreationAllowed)
                && Objects.equals(this.isAutoCreation, that.isAutoCreation)
                && Objects.equals(this.isAutoUpdate, that.isAutoUpdate)
                && Objects.equals(this.autoLinking, that.autoLinking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.isLinkingAllowed, this.isCreationAllowed, this.isAutoCreation, this.isAutoUpdate, this.autoLinking);
    }

    @Override
    public String toString() {
        return "ProviderOptions[" + "isLinkingAllowed=" + this.isLinkingAllowed + ", " + "isCreationAllowed=" + this.isCreationAllowed + ", " + "isAutoCreation=" + this.isAutoCreation + ", " + "isAutoUpdate=" + this.isAutoUpdate + ", " + "autoLinking=" + this.autoLinking + "]";
    }

    /**
     * Creates a new ProviderOptions.
     *
     * @param isLinkingAllowed whether linking to an existing account is allowed
     * @param isCreationAllowed whether creating a new account is allowed
     * @param isAutoCreation whether an account is created automatically
     * @param isAutoUpdate whether a linked account is updated automatically
     * @param autoLinking automatic linking behavior
     */
    @JsonCreator
    public ProviderOptions(Boolean isLinkingAllowed, Boolean isCreationAllowed, Boolean isAutoCreation, Boolean isAutoUpdate, IdpAutoLinking autoLinking) {
        this.isLinkingAllowed = isLinkingAllowed;
        this.isCreationAllowed = isCreationAllowed;
        this.isAutoCreation = isAutoCreation;
        this.isAutoUpdate = isAutoUpdate;
        this.autoLinking = autoLinking;
    }

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
