package com.mxraven.admin.model;

import java.util.List;
/**
 * Request body for {@code POST /v2/tenants/{slug}/identity-providers/apple}.
 *
 * @param idpRef immutable identity-provider reference
 * @param name human-readable provider name
 * @param clientId Apple service client identifier
 * @param teamId Apple developer team identifier
 * @param keyId Apple signing key identifier
 * @param privateKey Apple signing private key
 * @param scopes optional OAuth scopes
 * @param providerOptions optional provider-specific options
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

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds a {@link CreateTenantAppleIdentityProviderRequest}. */
    public static final class Builder {
        private String idpRef;
        private String name;
        private String clientId;
        private String teamId;
        private String keyId;
        private String privateKey;
        private List<String> scopes;
        private ProviderOptions providerOptions;

        /**
         * Sets the immutable identity-provider reference.
         *
         * @param idpRef identity-provider reference
         * @return this builder
         */
        public Builder idpRef(String idpRef) {
            this.idpRef = idpRef;
            return this;
        }

        /**
         * Sets the human-readable provider name.
         *
         * @param name provider name
         * @return this builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the Apple service client identifier.
         *
         * @param clientId client identifier
         * @return this builder
         */
        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        /**
         * Sets the Apple developer team identifier.
         *
         * @param teamId team identifier
         * @return this builder
         */
        public Builder teamId(String teamId) {
            this.teamId = teamId;
            return this;
        }

        /**
         * Sets the Apple signing key identifier.
         *
         * @param keyId key identifier
         * @return this builder
         */
        public Builder keyId(String keyId) {
            this.keyId = keyId;
            return this;
        }

        /**
         * Sets the Apple signing private key.
         *
         * @param privateKey private key
         * @return this builder
         */
        public Builder privateKey(String privateKey) {
            this.privateKey = privateKey;
            return this;
        }

        /**
         * Sets the OAuth scopes.
         *
         * @param scopes OAuth scopes
         * @return this builder
         */
        public Builder scopes(List<String> scopes) {
            this.scopes = scopes;
            return this;
        }

        /**
         * Sets the provider-specific options.
         *
         * @param providerOptions provider options
         * @return this builder
         */
        public Builder providerOptions(ProviderOptions providerOptions) {
            this.providerOptions = providerOptions;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return new request
         * @throws IllegalArgumentException when a field is missing or invalid
         */
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
