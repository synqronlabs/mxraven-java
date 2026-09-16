package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * LDAP attribute-name mapping.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class LdapAttributes {
    private final String idAttribute;
    private final String firstNameAttribute;
    private final String lastNameAttribute;
    private final String displayNameAttribute;
    private final String nickNameAttribute;
    private final String preferredUsernameAttribute;
    private final String emailAttribute;
    private final String emailVerifiedAttribute;
    private final String phoneAttribute;
    private final String phoneVerifiedAttribute;
    private final String preferredLanguageAttribute;
    private final String avatarUrlAttribute;
    private final String profileAttribute;

    /** LDAP attribute mapped to the identifier */
    public String idAttribute() {
        return idAttribute;
    }

    /** LDAP attribute mapped to the given name */
    public String firstNameAttribute() {
        return firstNameAttribute;
    }

    /** LDAP attribute mapped to the family name */
    public String lastNameAttribute() {
        return lastNameAttribute;
    }

    /** LDAP attribute mapped to the display name */
    public String displayNameAttribute() {
        return displayNameAttribute;
    }

    /** LDAP attribute mapped to the nickname */
    public String nickNameAttribute() {
        return nickNameAttribute;
    }

    /** LDAP attribute mapped to the preferred username */
    public String preferredUsernameAttribute() {
        return preferredUsernameAttribute;
    }

    /** LDAP attribute mapped to the email address */
    public String emailAttribute() {
        return emailAttribute;
    }

    /** LDAP attribute mapped to the email verification flag */
    public String emailVerifiedAttribute() {
        return emailVerifiedAttribute;
    }

    /** LDAP attribute mapped to the phone number */
    public String phoneAttribute() {
        return phoneAttribute;
    }

    /** LDAP attribute mapped to the phone verification flag */
    public String phoneVerifiedAttribute() {
        return phoneVerifiedAttribute;
    }

    /** LDAP attribute mapped to the preferred language */
    public String preferredLanguageAttribute() {
        return preferredLanguageAttribute;
    }

    /** LDAP attribute mapped to the avatar URL */
    public String avatarUrlAttribute() {
        return avatarUrlAttribute;
    }

    /** LDAP attribute mapped to the profile */
    public String profileAttribute() {
        return profileAttribute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LdapAttributes that = (LdapAttributes) o;
        return Objects.equals(this.idAttribute, that.idAttribute)
                && Objects.equals(this.firstNameAttribute, that.firstNameAttribute)
                && Objects.equals(this.lastNameAttribute, that.lastNameAttribute)
                && Objects.equals(this.displayNameAttribute, that.displayNameAttribute)
                && Objects.equals(this.nickNameAttribute, that.nickNameAttribute)
                && Objects.equals(this.preferredUsernameAttribute, that.preferredUsernameAttribute)
                && Objects.equals(this.emailAttribute, that.emailAttribute)
                && Objects.equals(this.emailVerifiedAttribute, that.emailVerifiedAttribute)
                && Objects.equals(this.phoneAttribute, that.phoneAttribute)
                && Objects.equals(this.phoneVerifiedAttribute, that.phoneVerifiedAttribute)
                && Objects.equals(this.preferredLanguageAttribute, that.preferredLanguageAttribute)
                && Objects.equals(this.avatarUrlAttribute, that.avatarUrlAttribute)
                && Objects.equals(this.profileAttribute, that.profileAttribute);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idAttribute, this.firstNameAttribute, this.lastNameAttribute, this.displayNameAttribute, this.nickNameAttribute, this.preferredUsernameAttribute, this.emailAttribute, this.emailVerifiedAttribute, this.phoneAttribute, this.phoneVerifiedAttribute, this.preferredLanguageAttribute, this.avatarUrlAttribute, this.profileAttribute);
    }

    @Override
    public String toString() {
        return "LdapAttributes[" + "idAttribute=" + this.idAttribute + ", " + "firstNameAttribute=" + this.firstNameAttribute + ", " + "lastNameAttribute=" + this.lastNameAttribute + ", " + "displayNameAttribute=" + this.displayNameAttribute + ", " + "nickNameAttribute=" + this.nickNameAttribute + ", " + "preferredUsernameAttribute=" + this.preferredUsernameAttribute + ", " + "emailAttribute=" + this.emailAttribute + ", " + "emailVerifiedAttribute=" + this.emailVerifiedAttribute + ", " + "phoneAttribute=" + this.phoneAttribute + ", " + "phoneVerifiedAttribute=" + this.phoneVerifiedAttribute + ", " + "preferredLanguageAttribute=" + this.preferredLanguageAttribute + ", " + "avatarUrlAttribute=" + this.avatarUrlAttribute + ", " + "profileAttribute=" + this.profileAttribute + "]";
    }

    /**
     * Creates a new LdapAttributes.
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
    @JsonCreator
    public LdapAttributes(String idAttribute, String firstNameAttribute, String lastNameAttribute, String displayNameAttribute, String nickNameAttribute, String preferredUsernameAttribute, String emailAttribute, String emailVerifiedAttribute, String phoneAttribute, String phoneVerifiedAttribute, String preferredLanguageAttribute, String avatarUrlAttribute, String profileAttribute) {
        this.idAttribute = idAttribute;
        this.firstNameAttribute = firstNameAttribute;
        this.lastNameAttribute = lastNameAttribute;
        this.displayNameAttribute = displayNameAttribute;
        this.nickNameAttribute = nickNameAttribute;
        this.preferredUsernameAttribute = preferredUsernameAttribute;
        this.emailAttribute = emailAttribute;
        this.emailVerifiedAttribute = emailVerifiedAttribute;
        this.phoneAttribute = phoneAttribute;
        this.phoneVerifiedAttribute = phoneVerifiedAttribute;
        this.preferredLanguageAttribute = preferredLanguageAttribute;
        this.avatarUrlAttribute = avatarUrlAttribute;
        this.profileAttribute = profileAttribute;
    }

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
