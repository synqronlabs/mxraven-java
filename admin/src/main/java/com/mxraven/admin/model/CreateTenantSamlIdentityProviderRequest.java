package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for <code>POST /v2/tenants/{slug}/identity-providers/saml</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CreateTenantSamlIdentityProviderRequest {
    private final String idpRef;
    private final String name;
    private final String metadataUrl;
    private final String metadataXml;
    private final SamlBinding binding;
    private final Boolean withSignedRequest;
    private final SamlNameIdFormat nameIdFormat;
    private final String transientMappingAttributeName;
    private final Boolean federatedLogoutEnabled;
    private final SamlSignatureAlgorithm signatureAlgorithm;
    private final ProviderOptions providerOptions;

    /** immutable identity-provider reference */
    public String idpRef() {
        return idpRef;
    }

    /** human-readable provider name */
    public String name() {
        return name;
    }

    /** optional SAML metadata URL */
    public String metadataUrl() {
        return metadataUrl;
    }

    /** optional inline SAML metadata XML */
    public String metadataXml() {
        return metadataXml;
    }

    /** SAML binding */
    public SamlBinding binding() {
        return binding;
    }

    /** optional flag to sign authentication requests */
    public Boolean withSignedRequest() {
        return withSignedRequest;
    }

    /** SAML NameID format */
    public SamlNameIdFormat nameIdFormat() {
        return nameIdFormat;
    }

    /** optional attribute mapped to transient NameIDs */
    public String transientMappingAttributeName() {
        return transientMappingAttributeName;
    }

    /** optional flag to enable federated logout */
    public Boolean federatedLogoutEnabled() {
        return federatedLogoutEnabled;
    }

    /** SAML signature algorithm */
    public SamlSignatureAlgorithm signatureAlgorithm() {
        return signatureAlgorithm;
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
        CreateTenantSamlIdentityProviderRequest that = (CreateTenantSamlIdentityProviderRequest) o;
        return Objects.equals(this.idpRef, that.idpRef)
                && Objects.equals(this.name, that.name)
                && Objects.equals(this.metadataUrl, that.metadataUrl)
                && Objects.equals(this.metadataXml, that.metadataXml)
                && Objects.equals(this.binding, that.binding)
                && Objects.equals(this.withSignedRequest, that.withSignedRequest)
                && Objects.equals(this.nameIdFormat, that.nameIdFormat)
                && Objects.equals(this.transientMappingAttributeName, that.transientMappingAttributeName)
                && Objects.equals(this.federatedLogoutEnabled, that.federatedLogoutEnabled)
                && Objects.equals(this.signatureAlgorithm, that.signatureAlgorithm)
                && Objects.equals(this.providerOptions, that.providerOptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idpRef, this.name, this.metadataUrl, this.metadataXml, this.binding, this.withSignedRequest, this.nameIdFormat, this.transientMappingAttributeName, this.federatedLogoutEnabled, this.signatureAlgorithm, this.providerOptions);
    }

    @Override
    public String toString() {
        return "CreateTenantSamlIdentityProviderRequest[" + "idpRef=" + this.idpRef + ", " + "name=" + this.name + ", " + "metadataUrl=" + this.metadataUrl + ", " + "metadataXml=" + this.metadataXml + ", " + "binding=" + this.binding + ", " + "withSignedRequest=" + this.withSignedRequest + ", " + "nameIdFormat=" + this.nameIdFormat + ", " + "transientMappingAttributeName=" + this.transientMappingAttributeName + ", " + "federatedLogoutEnabled=" + this.federatedLogoutEnabled + ", " + "signatureAlgorithm=" + this.signatureAlgorithm + ", " + "providerOptions=" + this.providerOptions + "]";
    }

    /**
     * Creates a new CreateTenantSamlIdentityProviderRequest.
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
    @JsonCreator
    public CreateTenantSamlIdentityProviderRequest(String idpRef, String name, String metadataUrl, String metadataXml, SamlBinding binding, Boolean withSignedRequest, SamlNameIdFormat nameIdFormat, String transientMappingAttributeName, Boolean federatedLogoutEnabled, SamlSignatureAlgorithm signatureAlgorithm, ProviderOptions providerOptions) {
        this.idpRef = idpRef;
        this.name = name;
        this.metadataUrl = metadataUrl;
        this.metadataXml = metadataXml;
        this.binding = binding;
        this.withSignedRequest = withSignedRequest;
        this.nameIdFormat = nameIdFormat;
        this.transientMappingAttributeName = transientMappingAttributeName;
        this.federatedLogoutEnabled = federatedLogoutEnabled;
        this.signatureAlgorithm = signatureAlgorithm;
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
