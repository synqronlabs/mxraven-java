package com.mxraven.admin.model;

/**
 * Request body for {@code POST /v2/tenants/{slug}/identity-providers/jwt}.
 *
 * @param idpRef immutable identity-provider reference
 * @param name human-readable provider name
 * @param issuer JWT issuer
 * @param jwtEndpoint JWT verification endpoint
 * @param keysEndpoint JWKS endpoint
 * @param headerName optional header carrying the token
 * @param audience optional expected token audience
 * @param providerOptions optional provider-specific options
 */
public record CreateTenantJwtIdentityProviderRequest(
        String idpRef,
        String name,
        String issuer,
        String jwtEndpoint,
        String keysEndpoint,
        String headerName,
        String audience,
        ProviderOptions providerOptions) {

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds a {@link CreateTenantJwtIdentityProviderRequest}. */
    public static final class Builder {
        private String idpRef;
        private String name;
        private String issuer;
        private String jwtEndpoint;
        private String keysEndpoint;
        private String headerName;
        private String audience;
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
         * Sets the JWT issuer.
         *
         * @param issuer issuer
         * @return this builder
         */
        public Builder issuer(String issuer) {
            this.issuer = issuer;
            return this;
        }

        /**
         * Sets the JWT verification endpoint.
         *
         * @param jwtEndpoint JWT endpoint
         * @return this builder
         */
        public Builder jwtEndpoint(String jwtEndpoint) {
            this.jwtEndpoint = jwtEndpoint;
            return this;
        }

        /**
         * Sets the JWKS endpoint.
         *
         * @param keysEndpoint keys endpoint
         * @return this builder
         */
        public Builder keysEndpoint(String keysEndpoint) {
            this.keysEndpoint = keysEndpoint;
            return this;
        }

        /**
         * Sets the header carrying the token.
         *
         * @param headerName header name
         * @return this builder
         */
        public Builder headerName(String headerName) {
            this.headerName = headerName;
            return this;
        }

        /**
         * Sets the expected token audience.
         *
         * @param audience audience
         * @return this builder
         */
        public Builder audience(String audience) {
            this.audience = audience;
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
        public CreateTenantJwtIdentityProviderRequest build() {
            RequestSupport.requireRef(idpRef, "idp_ref", 100);
            RequestSupport.requireString(name, "name", 255);
            RequestSupport.requireString(issuer, "issuer", 2048);
            RequestSupport.requireString(jwtEndpoint, "jwt_endpoint", 2048);
            RequestSupport.requireString(keysEndpoint, "keys_endpoint", 2048);
            RequestSupport.optionalString(headerName, "header_name", 255);
            RequestSupport.optionalString(audience, "audience", 255);
            return new CreateTenantJwtIdentityProviderRequest(idpRef, name, issuer, jwtEndpoint, keysEndpoint, headerName, audience, providerOptions);
        }
    }
}
