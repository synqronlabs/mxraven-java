package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Identity-provider type. */
public enum IdentityProviderType {
    OIDC("oidc"),
    OAUTH("oauth"),
    JWT("jwt"),
    SAML("saml"),
    LDAP("ldap"),
    GOOGLE("google"),
    AZURE_AD("azure_ad"),
    GITHUB("github"),
    GITHUB_ENTERPRISE_SERVER("github_enterprise_server"),
    GITLAB("gitlab"),
    GITLAB_SELF_HOSTED("gitlab_self_hosted"),
    APPLE("apple");

    private final String wire;

    IdentityProviderType(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

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
