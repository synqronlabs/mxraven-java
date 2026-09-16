package com.mxraven.admin.model;

import java.util.List;
/**
 * Request body for {@code POST /v2/tenants/{slug}/identity-providers/azure-ad}.
 *
 * @param idpRef immutable identity-provider reference
 * @param name human-readable provider name
 * @param clientId Azure AD client identifier
 * @param clientSecret Azure AD client secret
 * @param tenantId optional Azure AD tenant identifier
 * @param tenantType optional Azure AD tenant type
 * @param emailVerified optional email-verified flag
 * @param scopes optional OAuth scopes
 * @param providerOptions optional provider-specific options
 */
public record CreateTenantAzureAdIdentityProviderRequest(
        String idpRef,
        String name,
        String clientId,
        String clientSecret,
        String tenantId,
        AzureAdTenantType tenantType,
        Boolean emailVerified,
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

    /** Builds a {@link CreateTenantAzureAdIdentityProviderRequest}. */
    public static final class Builder {
        private String idpRef;
        private String name;
        private String clientId;
        private String clientSecret;
        private String tenantId;
        private AzureAdTenantType tenantType;
        private Boolean emailVerified;
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
         * Sets the Azure AD client identifier.
         *
         * @param clientId client identifier
         * @return this builder
         */
        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        /**
         * Sets the Azure AD client secret.
         *
         * @param clientSecret client secret
         * @return this builder
         */
        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        /**
         * Sets the Azure AD tenant identifier.
         *
         * @param tenantId tenant identifier
         * @return this builder
         */
        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        /**
         * Sets the Azure AD tenant type.
         *
         * @param tenantType tenant type
         * @return this builder
         */
        public Builder tenantType(AzureAdTenantType tenantType) {
            this.tenantType = tenantType;
            return this;
        }

        /**
         * Sets whether email addresses are treated as verified.
         *
         * @param emailVerified email-verified flag
         * @return this builder
         */
        public Builder emailVerified(Boolean emailVerified) {
            this.emailVerified = emailVerified;
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
        public CreateTenantAzureAdIdentityProviderRequest build() {
            RequestSupport.requireRef(idpRef, "idp_ref", 100);
            RequestSupport.requireString(name, "name", 255);
            RequestSupport.requireString(clientId, "client_id", 255);
            RequestSupport.requireString(clientSecret, "client_secret", 8192);
            RequestSupport.optionalString(tenantId, "tenant_id", 255);
            RequestSupport.optionalList(scopes, "scopes", 100);
            return new CreateTenantAzureAdIdentityProviderRequest(idpRef, name, clientId, clientSecret, tenantId, tenantType, emailVerified, scopes, providerOptions);
        }
    }
}
