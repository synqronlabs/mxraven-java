package com.mxraven.admin.model;

import java.util.List;
/**
 * Request body for {@code POST /v2/tenants/{slug}/identity-providers}.
 */
public record CreateTenantIdentityProviderRequest(
        String idpRef,
        String displayName,
        IdentityProviderType providerType,
        String issuer,
        String clientId,
        String clientSecret,
        List<String> scopes,
        Boolean isIdTokenMapping,
        Boolean usePkce,
        ProviderOptions providerOptions) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String idpRef;
        private String displayName;
        private IdentityProviderType providerType;
        private String issuer;
        private String clientId;
        private String clientSecret;
        private List<String> scopes;
        private Boolean isIdTokenMapping;
        private Boolean usePkce;
        private ProviderOptions providerOptions;

        public Builder idpRef(String idpRef) {
            this.idpRef = idpRef;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder providerType(IdentityProviderType providerType) {
            this.providerType = providerType;
            return this;
        }

        public Builder issuer(String issuer) {
            this.issuer = issuer;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder scopes(List<String> scopes) {
            this.scopes = scopes;
            return this;
        }

        public Builder isIdTokenMapping(Boolean isIdTokenMapping) {
            this.isIdTokenMapping = isIdTokenMapping;
            return this;
        }

        public Builder usePkce(Boolean usePkce) {
            this.usePkce = usePkce;
            return this;
        }

        public Builder providerOptions(ProviderOptions providerOptions) {
            this.providerOptions = providerOptions;
            return this;
        }

        public CreateTenantIdentityProviderRequest build() {
            RequestSupport.requireRef(idpRef, "idp_ref", 100);
            RequestSupport.requireString(displayName, "display_name", 255);
            RequestSupport.requireString(issuer, "issuer", 2048);
            RequestSupport.requireString(clientId, "client_id", 255);
            RequestSupport.requireString(clientSecret, "client_secret", 8192);
            RequestSupport.optionalList(scopes, "scopes", 100);
            return new CreateTenantIdentityProviderRequest(idpRef, displayName, providerType, issuer, clientId, clientSecret, scopes, isIdTokenMapping, usePkce, providerOptions);
        }
    }
}
