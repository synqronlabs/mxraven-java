package com.mxraven.admin.model;

import java.util.List;
/**
 * Request body for {@code POST /v2/tenants/{slug}/identity-providers/github}.
 *
 * @param idpRef immutable identity-provider reference
 * @param name human-readable provider name
 * @param clientId GitHub client identifier
 * @param clientSecret GitHub client secret
 * @param scopes optional OAuth scopes
 * @param providerOptions optional provider-specific options
 */
public record CreateTenantGitHubIdentityProviderRequest(
        String idpRef,
        String name,
        String clientId,
        String clientSecret,
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

    /** Builds a {@link CreateTenantGitHubIdentityProviderRequest}. */
    public static final class Builder {
        private String idpRef;
        private String name;
        private String clientId;
        private String clientSecret;
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
         * Sets the GitHub client identifier.
         *
         * @param clientId client identifier
         * @return this builder
         */
        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        /**
         * Sets the GitHub client secret.
         *
         * @param clientSecret client secret
         * @return this builder
         */
        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
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
