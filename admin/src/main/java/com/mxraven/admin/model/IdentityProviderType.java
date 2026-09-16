package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Identity-provider type. */
public enum IdentityProviderType {
    /** OpenID Connect. */
    OIDC("oidc"),
    /** OAuth 2.0. */
    OAUTH("oauth"),
    /** JWT bearer. */
    JWT("jwt"),
    /** SAML. */
    SAML("saml"),
    /** LDAP. */
    LDAP("ldap"),
    /** Google. */
    GOOGLE("google"),
    /** Azure AD. */
    AZURE_AD("azure_ad"),
    /** GitHub. */
    GITHUB("github"),
    /** GitHub Enterprise Server. */
    GITHUB_ENTERPRISE_SERVER("github_enterprise_server"),
    /** GitLab. */
    GITLAB("gitlab"),
    /** GitLab self-hosted. */
    GITLAB_SELF_HOSTED("gitlab_self_hosted"),
    /** Apple. */
    APPLE("apple");

    private final String wire;

    IdentityProviderType(String wire) {
        this.wire = wire;
    }

    /**
     * The wire value.
     *
     * @return the wire value
     */
    @JsonValue
    public String wire() {
        return wire;
    }

    /**
     * Resolves the provider type from its wire value.
     *
     * @param value wire value
     * @return matching provider type, or {@code null} when {@code value} is {@code null}
     * @throws IllegalArgumentException when the value is unknown
     */
    @JsonCreator
    public static IdentityProviderType fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (IdentityProviderType candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown IdentityProviderType value: " + value);
    }
}
