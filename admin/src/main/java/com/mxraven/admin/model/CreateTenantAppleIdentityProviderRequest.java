package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;
/**
 * Request body for <code>POST /v2/tenants/{slug}/identity-providers/apple</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CreateTenantAppleIdentityProviderRequest {
    private final String idpRef;
    private final String name;
    private final String clientId;
    private final String teamId;
    private final String keyId;
    private final String privateKey;
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

    /** Apple service client identifier */
    public String clientId() {
        return clientId;
    }

    /** Apple developer team identifier */
    public String teamId() {
        return teamId;
    }

    /** Apple signing key identifier */
    public String keyId() {
        return keyId;
    }

    /** Apple signing private key */
    public String privateKey() {
        return privateKey;
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
        CreateTenantAppleIdentityProviderRequest that = (CreateTenantAppleIdentityProviderRequest) o;
        return Objects.equals(this.idpRef, that.idpRef)
                && Objects.equals(this.name, that.name)
                && Objects.equals(this.clientId, that.clientId)
                && Objects.equals(this.teamId, that.teamId)
                && Objects.equals(this.keyId, that.keyId)
                && Objects.equals(this.privateKey, that.privateKey)
                && Objects.equals(this.scopes, that.scopes)
                && Objects.equals(this.providerOptions, that.providerOptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idpRef, this.name, this.clientId, this.teamId, this.keyId, this.privateKey, this.scopes, this.providerOptions);
    }

    @Override
    public String toString() {
        return "CreateTenantAppleIdentityProviderRequest[" + "idpRef=" + this.idpRef + ", " + "name=" + this.name + ", " + "clientId=" + this.clientId + ", " + "teamId=" + this.teamId + ", " + "keyId=" + this.keyId + ", " + "privateKey=" + this.privateKey + ", " + "scopes=" + this.scopes + ", " + "providerOptions=" + this.providerOptions + "]";
    }

    /**
     * Creates a new CreateTenantAppleIdentityProviderRequest.
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
    @JsonCreator
    public CreateTenantAppleIdentityProviderRequest(String idpRef, String name, String clientId, String teamId, String keyId, String privateKey, List<String> scopes, ProviderOptions providerOptions) {
        this.idpRef = idpRef;
        this.name = name;
        this.clientId = clientId;
        this.teamId = teamId;
        this.keyId = keyId;
        this.privateKey = privateKey;
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
