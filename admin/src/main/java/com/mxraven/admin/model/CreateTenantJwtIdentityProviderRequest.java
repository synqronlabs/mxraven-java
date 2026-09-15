package com.mxraven.admin.model;

/**
 * Request body for {@code POST /v2/tenants/{slug}/identity-providers/jwt}.
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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String idpRef;
        private String name;
        private String issuer;
        private String jwtEndpoint;
        private String keysEndpoint;
        private String headerName;
        private String audience;
        private ProviderOptions providerOptions;

        public Builder idpRef(String idpRef) {
            this.idpRef = idpRef;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder issuer(String issuer) {
            this.issuer = issuer;
            return this;
        }

        public Builder jwtEndpoint(String jwtEndpoint) {
            this.jwtEndpoint = jwtEndpoint;
            return this;
        }

        public Builder keysEndpoint(String keysEndpoint) {
            this.keysEndpoint = keysEndpoint;
            return this;
        }

        public Builder headerName(String headerName) {
            this.headerName = headerName;
            return this;
        }

        public Builder audience(String audience) {
            this.audience = audience;
            return this;
        }

        public Builder providerOptions(ProviderOptions providerOptions) {
            this.providerOptions = providerOptions;
            return this;
        }

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
