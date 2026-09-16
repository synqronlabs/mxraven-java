package com.mxraven.admin.model;

/**
 * LDAP attribute-name mapping.
 *
 * @param idAttribute LDAP attribute mapped to the identifier
 * @param firstNameAttribute LDAP attribute mapped to the given name
 * @param lastNameAttribute LDAP attribute mapped to the family name
 * @param displayNameAttribute LDAP attribute mapped to the display name
 * @param nickNameAttribute LDAP attribute mapped to the nickname
 * @param preferredUsernameAttribute LDAP attribute mapped to the preferred username
 * @param emailAttribute LDAP attribute mapped to the email address
 * @param emailVerifiedAttribute LDAP attribute mapped to the email verification flag
 * @param phoneAttribute LDAP attribute mapped to the phone number
 * @param phoneVerifiedAttribute LDAP attribute mapped to the phone verification flag
 * @param preferredLanguageAttribute LDAP attribute mapped to the preferred language
 * @param avatarUrlAttribute LDAP attribute mapped to the avatar URL
 * @param profileAttribute LDAP attribute mapped to the profile
 */
public record LdapAttributes(
        String idAttribute,
        String firstNameAttribute,
        String lastNameAttribute,
        String displayNameAttribute,
        String nickNameAttribute,
        String preferredUsernameAttribute,
        String emailAttribute,
        String emailVerifiedAttribute,
        String phoneAttribute,
        String phoneVerifiedAttribute,
        String preferredLanguageAttribute,
        String avatarUrlAttribute,
        String profileAttribute) {

    /**
     * Returns a new {@link Builder}.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link LdapAttributes} instances. */
    public static final class Builder {
        private String idAttribute;
        private String firstNameAttribute;
        private String lastNameAttribute;
        private String displayNameAttribute;
        private String nickNameAttribute;
        private String preferredUsernameAttribute;
        private String emailAttribute;
        private String emailVerifiedAttribute;
        private String phoneAttribute;
        private String phoneVerifiedAttribute;
        private String preferredLanguageAttribute;
        private String avatarUrlAttribute;
        private String profileAttribute;

        /**
         * Sets the identifier attribute mapping.
         *
         * @param idAttribute LDAP attribute mapped to the identifier
         * @return this builder
         */
        public Builder idAttribute(String idAttribute) {
            this.idAttribute = idAttribute;
            return this;
        }

        /**
         * Sets the given-name attribute mapping.
         *
         * @param firstNameAttribute LDAP attribute mapped to the given name
         * @return this builder
         */
        public Builder firstNameAttribute(String firstNameAttribute) {
            this.firstNameAttribute = firstNameAttribute;
            return this;
        }

        /**
         * Sets the family-name attribute mapping.
         *
         * @param lastNameAttribute LDAP attribute mapped to the family name
         * @return this builder
         */
        public Builder lastNameAttribute(String lastNameAttribute) {
            this.lastNameAttribute = lastNameAttribute;
            return this;
        }

        /**
         * Sets the display-name attribute mapping.
         *
         * @param displayNameAttribute LDAP attribute mapped to the display name
         * @return this builder
         */
        public Builder displayNameAttribute(String displayNameAttribute) {
            this.displayNameAttribute = displayNameAttribute;
            return this;
        }

        /**
         * Sets the nickname attribute mapping.
         *
         * @param nickNameAttribute LDAP attribute mapped to the nickname
         * @return this builder
         */
        public Builder nickNameAttribute(String nickNameAttribute) {
            this.nickNameAttribute = nickNameAttribute;
            return this;
        }

        /**
         * Sets the preferred-username attribute mapping.
         *
         * @param preferredUsernameAttribute LDAP attribute mapped to the preferred username
         * @return this builder
         */
        public Builder preferredUsernameAttribute(String preferredUsernameAttribute) {
            this.preferredUsernameAttribute = preferredUsernameAttribute;
            return this;
        }

        /**
         * Sets the email attribute mapping.
         *
         * @param emailAttribute LDAP attribute mapped to the email address
         * @return this builder
         */
        public Builder emailAttribute(String emailAttribute) {
            this.emailAttribute = emailAttribute;
            return this;
        }

        /**
         * Sets the email-verification attribute mapping.
         *
         * @param emailVerifiedAttribute LDAP attribute mapped to the email verification flag
         * @return this builder
         */
        public Builder emailVerifiedAttribute(String emailVerifiedAttribute) {
            this.emailVerifiedAttribute = emailVerifiedAttribute;
            return this;
        }

        /**
         * Sets the phone attribute mapping.
         *
         * @param phoneAttribute LDAP attribute mapped to the phone number
         * @return this builder
         */
        public Builder phoneAttribute(String phoneAttribute) {
            this.phoneAttribute = phoneAttribute;
            return this;
        }

        /**
         * Sets the phone-verification attribute mapping.
         *
         * @param phoneVerifiedAttribute LDAP attribute mapped to the phone verification flag
         * @return this builder
         */
        public Builder phoneVerifiedAttribute(String phoneVerifiedAttribute) {
            this.phoneVerifiedAttribute = phoneVerifiedAttribute;
            return this;
        }

        /**
         * Sets the preferred-language attribute mapping.
         *
         * @param preferredLanguageAttribute LDAP attribute mapped to the preferred language
         * @return this builder
         */
        public Builder preferredLanguageAttribute(String preferredLanguageAttribute) {
            this.preferredLanguageAttribute = preferredLanguageAttribute;
            return this;
        }

        /**
         * Sets the avatar-URL attribute mapping.
         *
         * @param avatarUrlAttribute LDAP attribute mapped to the avatar URL
         * @return this builder
         */
        public Builder avatarUrlAttribute(String avatarUrlAttribute) {
            this.avatarUrlAttribute = avatarUrlAttribute;
            return this;
        }

        /**
         * Sets the profile attribute mapping.
         *
         * @param profileAttribute LDAP attribute mapped to the profile
         * @return this builder
         */
        public Builder profileAttribute(String profileAttribute) {
            this.profileAttribute = profileAttribute;
            return this;
        }

        /**
         * Builds the configured attribute mapping.
         *
         * @return the built attributes
         */
        public LdapAttributes build() {
            return new LdapAttributes(idAttribute, firstNameAttribute, lastNameAttribute, displayNameAttribute, nickNameAttribute, preferredUsernameAttribute, emailAttribute, emailVerifiedAttribute, phoneAttribute, phoneVerifiedAttribute, preferredLanguageAttribute, avatarUrlAttribute, profileAttribute);
        }
    }
}
