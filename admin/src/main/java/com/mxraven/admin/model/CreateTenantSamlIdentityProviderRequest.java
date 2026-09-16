package com.mxraven.admin.model;

/**
 * Request body for {@code POST /v2/tenants/{slug}/identity-providers/saml}.
 *
 * @param idpRef immutable identity-provider reference
 * @param name human-readable provider name
 * @param metadataUrl optional SAML metadata URL
 * @param metadataXml optional inline SAML metadata XML
 * @param binding SAML binding
 * @param withSignedRequest optional flag to sign authentication requests
 * @param nameIdFormat SAML NameID format
 * @param transientMappingAttributeName optional attribute mapped to transient NameIDs
 * @param federatedLogoutEnabled optional flag to enable federated logout
 * @param signatureAlgorithm SAML signature algorithm
 * @param providerOptions optional provider-specific options
 */
public record CreateTenantSamlIdentityProviderRequest(
        String idpRef,
        String name,
        String metadataUrl,
        String metadataXml,
        SamlBinding binding,
        Boolean withSignedRequest,
        SamlNameIdFormat nameIdFormat,
        String transientMappingAttributeName,
        Boolean federatedLogoutEnabled,
        SamlSignatureAlgorithm signatureAlgorithm,
        ProviderOptions providerOptions) {

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds a {@link CreateTenantSamlIdentityProviderRequest}. */
    public static final class Builder {
        private String idpRef;
        private String name;
        private String metadataUrl;
        private String metadataXml;
        private SamlBinding binding;
        private Boolean withSignedRequest;
        private SamlNameIdFormat nameIdFormat;
        private String transientMappingAttributeName;
        private Boolean federatedLogoutEnabled;
        private SamlSignatureAlgorithm signatureAlgorithm;
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
         * Sets the SAML metadata URL.
         *
         * @param metadataUrl metadata URL
         * @return this builder
         */
        public Builder metadataUrl(String metadataUrl) {
            this.metadataUrl = metadataUrl;
            return this;
        }

        /**
         * Sets the inline SAML metadata XML.
         *
         * @param metadataXml metadata XML
         * @return this builder
         */
        public Builder metadataXml(String metadataXml) {
            this.metadataXml = metadataXml;
            return this;
        }

        /**
         * Sets the SAML binding.
         *
         * @param binding SAML binding
         * @return this builder
         */
        public Builder binding(SamlBinding binding) {
            this.binding = binding;
            return this;
        }

        /**
         * Sets whether authentication requests are signed.
         *
         * @param withSignedRequest signed-request flag
         * @return this builder
         */
        public Builder withSignedRequest(Boolean withSignedRequest) {
            this.withSignedRequest = withSignedRequest;
            return this;
        }

        /**
         * Sets the SAML NameID format.
         *
         * @param nameIdFormat NameID format
         * @return this builder
         */
        public Builder nameIdFormat(SamlNameIdFormat nameIdFormat) {
            this.nameIdFormat = nameIdFormat;
            return this;
        }

        /**
         * Sets the attribute mapped to transient NameIDs.
         *
         * @param transientMappingAttributeName attribute name
         * @return this builder
         */
        public Builder transientMappingAttributeName(String transientMappingAttributeName) {
            this.transientMappingAttributeName = transientMappingAttributeName;
            return this;
        }

        /**
         * Sets whether federated logout is enabled.
         *
         * @param federatedLogoutEnabled federated-logout flag
         * @return this builder
         */
        public Builder federatedLogoutEnabled(Boolean federatedLogoutEnabled) {
            this.federatedLogoutEnabled = federatedLogoutEnabled;
            return this;
        }

        /**
         * Sets the SAML signature algorithm.
         *
         * @param signatureAlgorithm signature algorithm
         * @return this builder
         */
        public Builder signatureAlgorithm(SamlSignatureAlgorithm signatureAlgorithm) {
            this.signatureAlgorithm = signatureAlgorithm;
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
        public CreateTenantSamlIdentityProviderRequest build() {
            RequestSupport.requireRef(idpRef, "idp_ref", 100);
            RequestSupport.requireString(name, "name", 255);
            RequestSupport.optionalString(metadataUrl, "metadata_url", 2048);
            RequestSupport.optionalString(metadataXml, "metadata_xml", 1000000);
            RequestSupport.optionalString(transientMappingAttributeName, "transient_mapping_attribute_name", 255);
            return new CreateTenantSamlIdentityProviderRequest(idpRef, name, metadataUrl, metadataXml, binding, withSignedRequest, nameIdFormat, transientMappingAttributeName, federatedLogoutEnabled, signatureAlgorithm, providerOptions);
        }
    }
}
