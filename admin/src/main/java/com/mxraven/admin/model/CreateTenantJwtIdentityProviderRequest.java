package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for <code>POST /v2/tenants/{slug}/identity-providers/jwt</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CreateTenantJwtIdentityProviderRequest {
    private final String idpRef;
    private final String name;
    private final String issuer;
    private final String jwtEndpoint;
    private final String keysEndpoint;
    private final String headerName;
    private final String audience;
    private final ProviderOptions providerOptions;

    /** immutable identity-provider reference */
    public String idpRef() {
        return idpRef;
    }

    /** human-readable provider name */
    public String name() {
        return name;
    }

    /** JWT issuer */
    public String issuer() {
        return issuer;
    }

    /** JWT verification endpoint */
    public String jwtEndpoint() {
        return jwtEndpoint;
    }

    /** JWKS endpoint */
    public String keysEndpoint() {
        return keysEndpoint;
    }

    /** optional header carrying the token */
    public String headerName() {
        return headerName;
    }

    /** optional expected token audience */
    public String audience() {
        return audience;
    }

    /** optional provider-specific options */
    public ProviderOptions providerOptions() {
        return providerOptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateTenantJwtIdentityProviderRequest that = (CreateTenantJwtIdentityProviderRequest) o;
        return Objects.equals(this.idpRef, that.idpRef)
                && Objects.equals(this.name, that.name)
                && Objects.equals(this.issuer, that.issuer)
                && Objects.equals(this.jwtEndpoint, that.jwtEndpoint)
                && Objects.equals(this.keysEndpoint, that.keysEndpoint)
                && Objects.equals(this.headerName, that.headerName)
                && Objects.equals(this.audience, that.audience)
                && Objects.equals(this.providerOptions, that.providerOptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idpRef, this.name, this.issuer, this.jwtEndpoint, this.keysEndpoint, this.headerName, this.audience, this.providerOptions);
    }

    @Override
    public String toString() {
        return "CreateTenantJwtIdentityProviderRequest[" + "idpRef=" + this.idpRef + ", " + "name=" + this.name + ", " + "issuer=" + this.issuer + ", " + "jwtEndpoint=" + this.jwtEndpoint + ", " + "keysEndpoint=" + this.keysEndpoint + ", " + "headerName=" + this.headerName + ", " + "audience=" + this.audience + ", " + "providerOptions=" + this.providerOptions + "]";
    }

    /**
     * Creates a new CreateTenantJwtIdentityProviderRequest.
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
    @JsonCreator
    public CreateTenantJwtIdentityProviderRequest(String idpRef, String name, String issuer, String jwtEndpoint, String keysEndpoint, String headerName, String audience, ProviderOptions providerOptions) {
        this.idpRef = idpRef;
        this.name = name;
        this.issuer = issuer;
        this.jwtEndpoint = jwtEndpoint;
        this.keysEndpoint = keysEndpoint;
        this.headerName = headerName;
        this.audience = audience;
        this.providerOptions = providerOptions;
    }

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
