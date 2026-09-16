package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;
/**
 * Request body for <code>POST /v2/tenants/{slug}/identity-providers/ldap</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CreateTenantLdapIdentityProviderRequest {
    private final String idpRef;
    private final String name;
    private final List<String> servers;
    private final Boolean startTls;
    private final String baseDn;
    private final String bindDn;
    private final String bindPassword;
    private final String userBase;
    private final List<String> userObjectClasses;
    private final List<String> userFilters;
    private final String timeout;
    private final LdapAttributes attributes;
    private final String rootCa;
    private final ProviderOptions providerOptions;

    /** immutable identity-provider reference */
    public String idpRef() {
        return idpRef;
    }

    /** human-readable provider name */
    public String name() {
        return name;
    }

    /** LDAP server URLs */
    public List<String> servers() {
        return servers;
    }

    /** optional flag to upgrade connections with StartTLS */
    public Boolean startTls() {
        return startTls;
    }

    /** optional base distinguished name */
    public String baseDn() {
        return baseDn;
    }

    /** optional bind distinguished name */
    public String bindDn() {
        return bindDn;
    }

    /** optional bind password */
    public String bindPassword() {
        return bindPassword;
    }

    /** optional user search base */
    public String userBase() {
        return userBase;
    }

    /** optional user object classes */
    public List<String> userObjectClasses() {
        return userObjectClasses;
    }

    /** optional user search filters */
    public List<String> userFilters() {
        return userFilters;
    }

    /** optional connection timeout */
    public String timeout() {
        return timeout;
    }

    /** optional attribute mapping */
    public LdapAttributes attributes() {
        return attributes;
    }

    /** optional root CA certificate */
    public String rootCa() {
        return rootCa;
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
        CreateTenantLdapIdentityProviderRequest that = (CreateTenantLdapIdentityProviderRequest) o;
        return Objects.equals(this.idpRef, that.idpRef)
                && Objects.equals(this.name, that.name)
                && Objects.equals(this.servers, that.servers)
                && Objects.equals(this.startTls, that.startTls)
                && Objects.equals(this.baseDn, that.baseDn)
                && Objects.equals(this.bindDn, that.bindDn)
                && Objects.equals(this.bindPassword, that.bindPassword)
                && Objects.equals(this.userBase, that.userBase)
                && Objects.equals(this.userObjectClasses, that.userObjectClasses)
                && Objects.equals(this.userFilters, that.userFilters)
                && Objects.equals(this.timeout, that.timeout)
                && Objects.equals(this.attributes, that.attributes)
                && Objects.equals(this.rootCa, that.rootCa)
                && Objects.equals(this.providerOptions, that.providerOptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idpRef, this.name, this.servers, this.startTls, this.baseDn, this.bindDn, this.bindPassword, this.userBase, this.userObjectClasses, this.userFilters, this.timeout, this.attributes, this.rootCa, this.providerOptions);
    }

    @Override
    public String toString() {
        return "CreateTenantLdapIdentityProviderRequest[" + "idpRef=" + this.idpRef + ", " + "name=" + this.name + ", " + "servers=" + this.servers + ", " + "startTls=" + this.startTls + ", " + "baseDn=" + this.baseDn + ", " + "bindDn=" + this.bindDn + ", " + "bindPassword=" + this.bindPassword + ", " + "userBase=" + this.userBase + ", " + "userObjectClasses=" + this.userObjectClasses + ", " + "userFilters=" + this.userFilters + ", " + "timeout=" + this.timeout + ", " + "attributes=" + this.attributes + ", " + "rootCa=" + this.rootCa + ", " + "providerOptions=" + this.providerOptions + "]";
    }

    /**
     * Creates a new CreateTenantLdapIdentityProviderRequest.
     *
     * @param idpRef immutable identity-provider reference
     * @param name human-readable provider name
     * @param servers LDAP server URLs
     * @param startTls optional flag to upgrade connections with StartTLS
     * @param baseDn optional base distinguished name
     * @param bindDn optional bind distinguished name
     * @param bindPassword optional bind password
     * @param userBase optional user search base
     * @param userObjectClasses optional user object classes
     * @param userFilters optional user search filters
     * @param timeout optional connection timeout
     * @param attributes optional attribute mapping
     * @param rootCa optional root CA certificate
     * @param providerOptions optional provider-specific options
     */
    @JsonCreator
    public CreateTenantLdapIdentityProviderRequest(String idpRef, String name, List<String> servers, Boolean startTls, String baseDn, String bindDn, String bindPassword, String userBase, List<String> userObjectClasses, List<String> userFilters, String timeout, LdapAttributes attributes, String rootCa, ProviderOptions providerOptions) {
        this.idpRef = idpRef;
        this.name = name;
        this.servers = servers;
        this.startTls = startTls;
        this.baseDn = baseDn;
        this.bindDn = bindDn;
        this.bindPassword = bindPassword;
        this.userBase = userBase;
        this.userObjectClasses = userObjectClasses;
        this.userFilters = userFilters;
        this.timeout = timeout;
        this.attributes = attributes;
        this.rootCa = rootCa;
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

    /** Builds a {@link CreateTenantLdapIdentityProviderRequest}. */
    public static final class Builder {
        private String idpRef;
        private String name;
        private List<String> servers;
        private Boolean startTls;
        private String baseDn;
        private String bindDn;
        private String bindPassword;
        private String userBase;
        private List<String> userObjectClasses;
        private List<String> userFilters;
        private String timeout;
        private LdapAttributes attributes;
        private String rootCa;
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
         * Sets the LDAP server URLs.
         *
         * @param servers server URLs
         * @return this builder
         */
        public Builder servers(List<String> servers) {
            this.servers = servers;
            return this;
        }

        /**
         * Sets whether to upgrade connections with StartTLS.
         *
         * @param startTls StartTLS flag
         * @return this builder
         */
        public Builder startTls(Boolean startTls) {
            this.startTls = startTls;
            return this;
        }

        /**
         * Sets the base distinguished name.
         *
         * @param baseDn base distinguished name
         * @return this builder
         */
        public Builder baseDn(String baseDn) {
            this.baseDn = baseDn;
            return this;
        }

        /**
         * Sets the bind distinguished name.
         *
         * @param bindDn bind distinguished name
         * @return this builder
         */
        public Builder bindDn(String bindDn) {
            this.bindDn = bindDn;
            return this;
        }

        /**
         * Sets the bind password.
         *
         * @param bindPassword bind password
         * @return this builder
         */
        public Builder bindPassword(String bindPassword) {
            this.bindPassword = bindPassword;
            return this;
        }

        /**
         * Sets the user search base.
         *
         * @param userBase user search base
         * @return this builder
         */
        public Builder userBase(String userBase) {
            this.userBase = userBase;
            return this;
        }

        /**
         * Sets the user object classes.
         *
         * @param userObjectClasses user object classes
         * @return this builder
         */
        public Builder userObjectClasses(List<String> userObjectClasses) {
            this.userObjectClasses = userObjectClasses;
            return this;
        }

        /**
         * Sets the user search filters.
         *
         * @param userFilters user search filters
         * @return this builder
         */
        public Builder userFilters(List<String> userFilters) {
            this.userFilters = userFilters;
            return this;
        }

        /**
         * Sets the connection timeout.
         *
         * @param timeout connection timeout
         * @return this builder
         */
        public Builder timeout(String timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * Sets the attribute mapping.
         *
         * @param attributes attribute mapping
         * @return this builder
         */
        public Builder attributes(LdapAttributes attributes) {
            this.attributes = attributes;
            return this;
        }

        /**
         * Sets the root CA certificate.
         *
         * @param rootCa root CA certificate
         * @return this builder
         */
        public Builder rootCa(String rootCa) {
            this.rootCa = rootCa;
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
        public CreateTenantLdapIdentityProviderRequest build() {
            RequestSupport.requireRef(idpRef, "idp_ref", 100);
            RequestSupport.requireString(name, "name", 255);
            RequestSupport.requireList(servers, "servers", 50);
            RequestSupport.optionalString(baseDn, "base_dn", 2048);
            RequestSupport.optionalString(bindDn, "bind_dn", 2048);
            RequestSupport.optionalString(bindPassword, "bind_password", 8192);
            RequestSupport.optionalString(userBase, "user_base", 2048);
            RequestSupport.optionalList(userObjectClasses, "user_object_classes", 100);
            RequestSupport.optionalList(userFilters, "user_filters", 100);
            RequestSupport.optionalString(timeout, "timeout", 32);
            RequestSupport.optionalString(rootCa, "root_ca", 65536);
            return new CreateTenantLdapIdentityProviderRequest(idpRef, name, servers, startTls, baseDn, bindDn, bindPassword, userBase, userObjectClasses, userFilters, timeout, attributes, rootCa, providerOptions);
        }
    }
}
