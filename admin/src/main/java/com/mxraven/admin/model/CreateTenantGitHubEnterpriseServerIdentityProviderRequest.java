package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;
/**
 * Request body for <code>POST /v2/tenants/{slug}/identity-providers/github-enterprise-server</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CreateTenantGitHubEnterpriseServerIdentityProviderRequest {
    private final String idpRef;
    private final String name;
    private final String clientId;
    private final String clientSecret;
    private final String authorizationEndpoint;
    private final String tokenEndpoint;
    private final String userEndpoint;
    private final List<String> scopes;
    private final ProviderOptions providerOptions;

    /** immutable identity-provider reference */
    public String idpRef() {
        return idpRef;
    }

    /** human-readable provider name */
    public String name() {
        return name;
    }

    /** GitHub Enterprise Server client identifier */
    public String clientId() {
        return clientId;
    }

    /** GitHub Enterprise Server client secret */
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
        CreateTenantGitHubEnterpriseServerIdentityProviderRequest that = (CreateTenantGitHubEnterpriseServerIdentityProviderRequest) o;
        return Objects.equals(this.idpRef, that.idpRef)
                && Objects.equals(this.name, that.name)
                && Objects.equals(this.clientId, that.clientId)
                && Objects.equals(this.clientSecret, that.clientSecret)
                && Objects.equals(this.authorizationEndpoint, that.authorizationEndpoint)
                && Objects.equals(this.tokenEndpoint, that.tokenEndpoint)
                && Objects.equals(this.userEndpoint, that.userEndpoint)
                && Objects.equals(this.scopes, that.scopes)
                && Objects.equals(this.providerOptions, that.providerOptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idpRef, this.name, this.clientId, this.clientSecret, this.authorizationEndpoint, this.tokenEndpoint, this.userEndpoint, this.scopes, this.providerOptions);
    }

    @Override
    public String toString() {
        return "CreateTenantGitHubEnterpriseServerIdentityProviderRequest[" + "idpRef=" + this.idpRef + ", " + "name=" + this.name + ", " + "clientId=" + this.clientId + ", " + "clientSecret=" + this.clientSecret + ", " + "authorizationEndpoint=" + this.authorizationEndpoint + ", " + "tokenEndpoint=" + this.tokenEndpoint + ", " + "userEndpoint=" + this.userEndpoint + ", " + "scopes=" + this.scopes + ", " + "providerOptions=" + this.providerOptions + "]";
    }

    /**
     * Creates a new CreateTenantGitHubEnterpriseServerIdentityProviderRequest.
     *
     * @param idpRef immutable identity-provider reference
     * @param name human-readable provider name
     * @param clientId GitHub Enterprise Server client identifier
     * @param clientSecret GitHub Enterprise Server client secret
     * @param authorizationEndpoint OAuth authorization endpoint
     * @param tokenEndpoint OAuth token endpoint
     * @param userEndpoint user-info endpoint
     * @param scopes optional OAuth scopes
     * @param providerOptions optional provider-specific options
     */
    @JsonCreator
    public CreateTenantGitHubEnterpriseServerIdentityProviderRequest(String idpRef, String name, String clientId, String clientSecret, String authorizationEndpoint, String tokenEndpoint, String userEndpoint, List<String> scopes, ProviderOptions providerOptions) {
        this.idpRef = idpRef;
        this.name = name;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authorizationEndpoint = authorizationEndpoint;
        this.tokenEndpoint = tokenEndpoint;
        this.userEndpoint = userEndpoint;
        this.scopes = scopes;
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

    /** Builds a {@link CreateTenantGitHubEnterpriseServerIdentityProviderRequest}. */
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
         * Sets the GitHub Enterprise Server client identifier.
         *
         * @param clientId client identifier
         * @return this builder
         */
        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        /**
         * Sets the GitHub Enterprise Server client secret.
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
