package com.mxraven.admin.model;

import java.util.List;
/**
 * Request body for {@code POST /v2/tenants/{slug}/identity-providers}.
 *
 * @param idpRef immutable identity-provider reference
 * @param displayName human-readable provider name
 * @param providerType identity-provider type
 * @param issuer OIDC issuer URL
 * @param clientId OAuth client identifier
 * @param clientSecret OAuth client secret
 * @param scopes optional OAuth scopes
 * @param isIdTokenMapping optional flag to map claims from the ID token
 * @param usePkce optional flag to use PKCE
 * @param providerOptions optional provider-specific options
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

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds a {@link CreateTenantIdentityProviderRequest}. */
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
         * @param displayName provider name
         * @return this builder
         */
        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * Sets the identity-provider type.
         *
         * @param providerType provider type
         * @return this builder
         */
        public Builder providerType(IdentityProviderType providerType) {
            this.providerType = providerType;
            return this;
        }

        /**
         * Sets the OIDC issuer URL.
         *
         * @param issuer issuer URL
         * @return this builder
         */
        public Builder issuer(String issuer) {
            this.issuer = issuer;
            return this;
        }

        /**
         * Sets the OAuth client identifier.
         *
         * @param clientId client identifier
         * @return this builder
         */
        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        /**
         * Sets the OAuth client secret.
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
         * Sets whether claims are mapped from the ID token.
         *
         * @param isIdTokenMapping ID-token-mapping flag
         * @return this builder
         */
        public Builder isIdTokenMapping(Boolean isIdTokenMapping) {
            this.isIdTokenMapping = isIdTokenMapping;
            return this;
        }

        /**
         * Sets whether PKCE is used.
         *
         * @param usePkce PKCE flag
         * @return this builder
         */
        public Builder usePkce(Boolean usePkce) {
            this.usePkce = usePkce;
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
