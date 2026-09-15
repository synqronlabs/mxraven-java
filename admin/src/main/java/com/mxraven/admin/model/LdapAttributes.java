package com.mxraven.admin.model;

/**
 * LDAP attribute-name mapping.
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

    public static Builder builder() {
        return new Builder();
    }

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

        public Builder idAttribute(String idAttribute) {
            this.idAttribute = idAttribute;
            return this;
        }

        public Builder firstNameAttribute(String firstNameAttribute) {
            this.firstNameAttribute = firstNameAttribute;
            return this;
        }

        public Builder lastNameAttribute(String lastNameAttribute) {
            this.lastNameAttribute = lastNameAttribute;
            return this;
        }

        public Builder displayNameAttribute(String displayNameAttribute) {
            this.displayNameAttribute = displayNameAttribute;
            return this;
        }

        public Builder nickNameAttribute(String nickNameAttribute) {
            this.nickNameAttribute = nickNameAttribute;
            return this;
        }

        public Builder preferredUsernameAttribute(String preferredUsernameAttribute) {
            this.preferredUsernameAttribute = preferredUsernameAttribute;
            return this;
        }

        public Builder emailAttribute(String emailAttribute) {
            this.emailAttribute = emailAttribute;
            return this;
        }

        public Builder emailVerifiedAttribute(String emailVerifiedAttribute) {
            this.emailVerifiedAttribute = emailVerifiedAttribute;
            return this;
        }

        public Builder phoneAttribute(String phoneAttribute) {
            this.phoneAttribute = phoneAttribute;
            return this;
        }

        public Builder phoneVerifiedAttribute(String phoneVerifiedAttribute) {
            this.phoneVerifiedAttribute = phoneVerifiedAttribute;
            return this;
        }

        public Builder preferredLanguageAttribute(String preferredLanguageAttribute) {
            this.preferredLanguageAttribute = preferredLanguageAttribute;
            return this;
        }

        public Builder avatarUrlAttribute(String avatarUrlAttribute) {
            this.avatarUrlAttribute = avatarUrlAttribute;
            return this;
        }

        public Builder profileAttribute(String profileAttribute) {
            this.profileAttribute = profileAttribute;
            return this;
        }

        public LdapAttributes build() {
            return new LdapAttributes(idAttribute, firstNameAttribute, lastNameAttribute, displayNameAttribute, nickNameAttribute, preferredUsernameAttribute, emailAttribute, emailVerifiedAttribute, phoneAttribute, phoneVerifiedAttribute, preferredLanguageAttribute, avatarUrlAttribute, profileAttribute);
        }
    }
}
