package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;
/**
 * Request body for <code>POST /v2/tenants/{slug}/identity-providers/oauth</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CreateTenantOAuthIdentityProviderRequest {
    private final String idpRef;
    private final String name;
    private final String clientId;
    private final String clientSecret;
    private final String authorizationEndpoint;
    private final String tokenEndpoint;
    private final String userEndpoint;
    private final List<String> scopes;
    private final String idAttribute;
    private final Boolean usePkce;
    private final ProviderOptions providerOptions;

    /** immutable identity-provider reference */
    public String idpRef() {
        return idpRef;
    }

    /** human-readable provider name */
    public String name() {
        return name;
    }

    /** OAuth client identifier */
    public String clientId() {
        return clientId;
    }

    /** OAuth client secret */
    public String clientSecret() {
        return clientSecret;
    }

    /** OAuth authorization endpoint */
    public String authorizationEndpoint() {
        return authorizationEndpoint;
    }

    /** OAuth token endpoint */
    public String tokenEndpoint() {
        return tokenEndpoint;
    }

    /** user-info endpoint */
    public String userEndpoint() {
        return userEndpoint;
    }

    /** optional OAuth scopes */
    public List<String> scopes() {
        return scopes;
    }

    /** optional claim used as the user identifier */
    public String idAttribute() {
        return idAttribute;
    }

    /** optional flag to use PKCE */
    public Boolean usePkce() {
        return usePkce;
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
        CreateTenantOAuthIdentityProviderRequest that = (CreateTenantOAuthIdentityProviderRequest) o;
        return Objects.equals(this.idpRef, that.idpRef)
                && Objects.equals(this.name, that.name)
                && Objects.equals(this.clientId, that.clientId)
                && Objects.equals(this.clientSecret, that.clientSecret)
                && Objects.equals(this.authorizationEndpoint, that.authorizationEndpoint)
                && Objects.equals(this.tokenEndpoint, that.tokenEndpoint)
                && Objects.equals(this.userEndpoint, that.userEndpoint)
                && Objects.equals(this.scopes, that.scopes)
                && Objects.equals(this.idAttribute, that.idAttribute)
                && Objects.equals(this.usePkce, that.usePkce)
                && Objects.equals(this.providerOptions, that.providerOptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idpRef, this.name, this.clientId, this.clientSecret, this.authorizationEndpoint, this.tokenEndpoint, this.userEndpoint, this.scopes, this.idAttribute, this.usePkce, this.providerOptions);
    }

    @Override
    public String toString() {
        return "CreateTenantOAuthIdentityProviderRequest[" + "idpRef=" + this.idpRef + ", " + "name=" + this.name + ", " + "clientId=" + this.clientId + ", " + "clientSecret=" + this.clientSecret + ", " + "authorizationEndpoint=" + this.authorizationEndpoint + ", " + "tokenEndpoint=" + this.tokenEndpoint + ", " + "userEndpoint=" + this.userEndpoint + ", " + "scopes=" + this.scopes + ", " + "idAttribute=" + this.idAttribute + ", " + "usePkce=" + this.usePkce + ", " + "providerOptions=" + this.providerOptions + "]";
    }

    /**
     * Creates a new CreateTenantOAuthIdentityProviderRequest.
     *
     * @param idpRef immutable identity-provider reference
     * @param name human-readable provider name
     * @param clientId OAuth client identifier
     * @param clientSecret OAuth client secret
     * @param authorizationEndpoint OAuth authorization endpoint
     * @param tokenEndpoint OAuth token endpoint
     * @param userEndpoint user-info endpoint
     * @param scopes optional OAuth scopes
     * @param idAttribute optional claim used as the user identifier
     * @param usePkce optional flag to use PKCE
     * @param providerOptions optional provider-specific options
     */
    @JsonCreator
    public CreateTenantOAuthIdentityProviderRequest(String idpRef, String name, String clientId, String clientSecret, String authorizationEndpoint, String tokenEndpoint, String userEndpoint, List<String> scopes, String idAttribute, Boolean usePkce, ProviderOptions providerOptions) {
        this.idpRef = idpRef;
        this.name = name;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authorizationEndpoint = authorizationEndpoint;
        this.tokenEndpoint = tokenEndpoint;
        this.userEndpoint = userEndpoint;
        this.scopes = scopes;
        this.idAttribute = idAttribute;
        this.usePkce = usePkce;
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

    /** Builds a {@link CreateTenantOAuthIdentityProviderRequest}. */
    public static final class Builder {
        private String idpRef;
        private String name;
        private String clientId;
        private String clientSecret;
        private String authorizationEndpoint;
        private String tokenEndpoint;
        private String userEndpoint;
        private List<String> scopes;
        private String idAttribute;
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
         * @param name provider name
         * @return this builder
         */
        public Builder name(String name) {
            this.name = name;
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
         * Sets the OAuth authorization endpoint.
         *
         * @param authorizationEndpoint authorization endpoint
         * @return this builder
         */
        public Builder authorizationEndpoint(String authorizationEndpoint) {
            this.authorizationEndpoint = authorizationEndpoint;
            return this;
        }

        /**
         * Sets the OAuth token endpoint.
         *
         * @param tokenEndpoint token endpoint
         * @return this builder
         */
        public Builder tokenEndpoint(String tokenEndpoint) {
            this.tokenEndpoint = tokenEndpoint;
            return this;
        }

        /**
         * Sets the user-info endpoint.
         *
         * @param userEndpoint user-info endpoint
         * @return this builder
         */
        public Builder userEndpoint(String userEndpoint) {
            this.userEndpoint = userEndpoint;
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
         * Sets the claim used as the user identifier.
         *
         * @param idAttribute identifier attribute
         * @return this builder
         */
        public Builder idAttribute(String idAttribute) {
            this.idAttribute = idAttribute;
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
        public CreateTenantOAuthIdentityProviderRequest build() {
            RequestSupport.requireRef(idpRef, "idp_ref", 100);
            RequestSupport.requireString(name, "name", 255);
            RequestSupport.requireString(clientId, "client_id", 255);
            RequestSupport.requireString(clientSecret, "client_secret", 8192);
            RequestSupport.requireString(authorizationEndpoint, "authorization_endpoint", 2048);
            RequestSupport.requireString(tokenEndpoint, "token_endpoint", 2048);
            RequestSupport.requireString(userEndpoint, "user_endpoint", 2048);
            RequestSupport.optionalList(scopes, "scopes", 100);
            RequestSupport.optionalString(idAttribute, "id_attribute", 255);
            return new CreateTenantOAuthIdentityProviderRequest(idpRef, name, clientId, clientSecret, authorizationEndpoint, tokenEndpoint, userEndpoint, scopes, idAttribute, usePkce, providerOptions);
        }
    }
}
