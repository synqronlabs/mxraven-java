package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;
/**
 * Request body for <code>POST /v2/tenants/{slug}/identity-providers/gitlab-self-hosted</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CreateTenantGitLabSelfHostedIdentityProviderRequest {
    private final String idpRef;
    private final String name;
    private final String issuer;
    private final String clientId;
    private final String clientSecret;
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

    /** GitLab instance issuer */
    public String issuer() {
        return issuer;
    }

    /** GitLab client identifier */
    public String clientId() {
        return clientId;
    }

    /** GitLab client secret */
    public String clientSecret() {
        return clientSecret;
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
        CreateTenantGitLabSelfHostedIdentityProviderRequest that = (CreateTenantGitLabSelfHostedIdentityProviderRequest) o;
        return Objects.equals(this.idpRef, that.idpRef)
                && Objects.equals(this.name, that.name)
                && Objects.equals(this.issuer, that.issuer)
                && Objects.equals(this.clientId, that.clientId)
                && Objects.equals(this.clientSecret, that.clientSecret)
                && Objects.equals(this.scopes, that.scopes)
                && Objects.equals(this.providerOptions, that.providerOptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idpRef, this.name, this.issuer, this.clientId, this.clientSecret, this.scopes, this.providerOptions);
    }

    @Override
    public String toString() {
        return "CreateTenantGitLabSelfHostedIdentityProviderRequest[" + "idpRef=" + this.idpRef + ", " + "name=" + this.name + ", " + "issuer=" + this.issuer + ", " + "clientId=" + this.clientId + ", " + "clientSecret=" + this.clientSecret + ", " + "scopes=" + this.scopes + ", " + "providerOptions=" + this.providerOptions + "]";
    }

    /**
     * Creates a new CreateTenantGitLabSelfHostedIdentityProviderRequest.
     *
     * @param idpRef immutable identity-provider reference
     * @param name human-readable provider name
     * @param issuer GitLab instance issuer
     * @param clientId GitLab client identifier
     * @param clientSecret GitLab client secret
     * @param scopes optional OAuth scopes
     * @param providerOptions optional provider-specific options
     */
    @JsonCreator
    public CreateTenantGitLabSelfHostedIdentityProviderRequest(String idpRef, String name, String issuer, String clientId, String clientSecret, List<String> scopes, ProviderOptions providerOptions) {
        this.idpRef = idpRef;
        this.name = name;
        this.issuer = issuer;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
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

    /** Builds a {@link CreateTenantGitLabSelfHostedIdentityProviderRequest}. */
    public static final class Builder {
        private String idpRef;
        private String name;
        private String issuer;
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
         * Sets the GitLab instance issuer.
         *
         * @param issuer issuer
         * @return this builder
         */
        public Builder issuer(String issuer) {
            this.issuer = issuer;
            return this;
        }

        /**
         * Sets the GitLab client identifier.
         *
         * @param clientId client identifier
         * @return this builder
         */
        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        /**
         * Sets the GitLab client secret.
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
        public CreateTenantGitLabSelfHostedIdentityProviderRequest build() {
            RequestSupport.requireRef(idpRef, "idp_ref", 100);
            RequestSupport.requireString(name, "name", 255);
            RequestSupport.requireString(issuer, "issuer", 2048);
            RequestSupport.requireString(clientId, "client_id", 255);
            RequestSupport.requireString(clientSecret, "client_secret", 8192);
            RequestSupport.optionalList(scopes, "scopes", 100);
            return new CreateTenantGitLabSelfHostedIdentityProviderRequest(idpRef, name, issuer, clientId, clientSecret, scopes, providerOptions);
        }
    }
}
