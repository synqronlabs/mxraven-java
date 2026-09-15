package com.mxraven.admin.model;

import java.util.List;
/**
 * Request body for {@code POST /v2/tenants/{slug}/identity-providers/github-enterprise-server}.
 */
public record CreateTenantGitHubEnterpriseServerIdentityProviderRequest(
        String idpRef,
        String name,
        String clientId,
        String clientSecret,
        String authorizationEndpoint,
        String tokenEndpoint,
        String userEndpoint,
        List<String> scopes,
        ProviderOptions providerOptions) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String idpRef;
        private String name;
        private String clientId;
        private String clientSecret;
        private String authorizationEndpoint;
        private String tokenEndpoint;
        private String userEndpoint;
        private List<String> scopes;
        private ProviderOptions providerOptions;

        public Builder idpRef(String idpRef) {
            this.idpRef = idpRef;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
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

        public Builder authorizationEndpoint(String authorizationEndpoint) {
            this.authorizationEndpoint = authorizationEndpoint;
            return this;
        }

        public Builder tokenEndpoint(String tokenEndpoint) {
            this.tokenEndpoint = tokenEndpoint;
            return this;
        }

        public Builder userEndpoint(String userEndpoint) {
            this.userEndpoint = userEndpoint;
            return this;
        }

        public Builder scopes(List<String> scopes) {
            this.scopes = scopes;
            return this;
        }

        public Builder providerOptions(ProviderOptions providerOptions) {
            this.providerOptions = providerOptions;
            return this;
        }

        public CreateTenantGitHubEnterpriseServerIdentityProviderRequest build() {
            RequestSupport.requireRef(idpRef, "idp_ref", 100);
            RequestSupport.requireString(name, "name", 255);
            RequestSupport.requireString(clientId, "client_id", 255);
            RequestSupport.requireString(clientSecret, "client_secret", 8192);
            RequestSupport.requireString(authorizationEndpoint, "authorization_endpoint", 2048);
            RequestSupport.requireString(tokenEndpoint, "token_endpoint", 2048);
            RequestSupport.requireString(userEndpoint, "user_endpoint", 2048);
            RequestSupport.optionalList(scopes, "scopes", 100);
            return new CreateTenantGitHubEnterpriseServerIdentityProviderRequest(idpRef, name, clientId, clientSecret, authorizationEndpoint, tokenEndpoint, userEndpoint, scopes, providerOptions);
        }
    }
}
