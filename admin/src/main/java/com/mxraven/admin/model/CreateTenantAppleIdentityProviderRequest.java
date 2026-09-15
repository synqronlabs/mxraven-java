package com.mxraven.admin.model;

import java.util.List;
/**
 * Request body for {@code POST /v2/tenants/{slug}/identity-providers/apple}.
 */
public record CreateTenantAppleIdentityProviderRequest(
        String idpRef,
        String name,
        String clientId,
        String teamId,
        String keyId,
        String privateKey,
        List<String> scopes,
        ProviderOptions providerOptions) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String idpRef;
        private String name;
        private String clientId;
        private String teamId;
        private String keyId;
        private String privateKey;
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

        public Builder teamId(String teamId) {
            this.teamId = teamId;
            return this;
        }

        public Builder keyId(String keyId) {
            this.keyId = keyId;
            return this;
        }

        public Builder privateKey(String privateKey) {
            this.privateKey = privateKey;
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

        public CreateTenantAppleIdentityProviderRequest build() {
            RequestSupport.requireRef(idpRef, "idp_ref", 100);
            RequestSupport.requireString(name, "name", 255);
            RequestSupport.requireString(clientId, "client_id", 255);
            RequestSupport.requireString(teamId, "team_id", 255);
            RequestSupport.requireString(keyId, "key_id", 255);
            RequestSupport.requireString(privateKey, "private_key", 8192);
            RequestSupport.optionalList(scopes, "scopes", 100);
            return new CreateTenantAppleIdentityProviderRequest(idpRef, name, clientId, teamId, keyId, privateKey, scopes, providerOptions);
        }
    }
}
