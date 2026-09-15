package com.mxraven.admin.model;

/**
 * Request body for {@code POST /v2/tenants/{slug}/identity-providers/saml}.
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

    public static Builder builder() {
        return new Builder();
    }

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

        public Builder idpRef(String idpRef) {
            this.idpRef = idpRef;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder metadataUrl(String metadataUrl) {
            this.metadataUrl = metadataUrl;
            return this;
        }

        public Builder metadataXml(String metadataXml) {
            this.metadataXml = metadataXml;
            return this;
        }

        public Builder binding(SamlBinding binding) {
            this.binding = binding;
            return this;
        }

        public Builder withSignedRequest(Boolean withSignedRequest) {
            this.withSignedRequest = withSignedRequest;
            return this;
        }

        public Builder nameIdFormat(SamlNameIdFormat nameIdFormat) {
            this.nameIdFormat = nameIdFormat;
            return this;
        }

        public Builder transientMappingAttributeName(String transientMappingAttributeName) {
            this.transientMappingAttributeName = transientMappingAttributeName;
            return this;
        }

        public Builder federatedLogoutEnabled(Boolean federatedLogoutEnabled) {
            this.federatedLogoutEnabled = federatedLogoutEnabled;
            return this;
        }

        public Builder signatureAlgorithm(SamlSignatureAlgorithm signatureAlgorithm) {
            this.signatureAlgorithm = signatureAlgorithm;
            return this;
        }

        public Builder providerOptions(ProviderOptions providerOptions) {
            this.providerOptions = providerOptions;
            return this;
        }

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
