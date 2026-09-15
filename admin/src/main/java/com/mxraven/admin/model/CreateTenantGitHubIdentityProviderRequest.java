package com.mxraven.admin.model;

import java.util.List;
/**
 * Request body for {@code POST /v2/tenants/{slug}/identity-providers/github}.
 */
public record CreateTenantGitHubIdentityProviderRequest(
        String idpRef,
        String name,
        String clientId,
        String clientSecret,
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

        public Builder scopes(List<String> scopes) {
            this.scopes = scopes;
            return this;
        }

        public Builder providerOptions(ProviderOptions providerOptions) {
            this.providerOptions = providerOptions;
            return this;
        }

        public CreateTenantGitHubIdentityProviderRequest build() {
            RequestSupport.requireRef(idpRef, "idp_ref", 100);
            RequestSupport.requireString(name, "name", 255);
            RequestSupport.requireString(clientId, "client_id", 255);
            RequestSupport.requireString(clientSecret, "client_secret", 8192);
            RequestSupport.optionalList(scopes, "scopes", 100);
            return new CreateTenantGitHubIdentityProviderRequest(idpRef, name, clientId, clientSecret, scopes, providerOptions);
        }
    }
}
