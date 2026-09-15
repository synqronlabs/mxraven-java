package com.mxraven.admin.model;

import java.util.List;
/**
 * Request body for {@code POST /v2/tenants/{slug}/identity-providers/ldap}.
 */
public record CreateTenantLdapIdentityProviderRequest(
        String idpRef,
        String name,
        List<String> servers,
        Boolean startTls,
        String baseDn,
        String bindDn,
        String bindPassword,
        String userBase,
        List<String> userObjectClasses,
        List<String> userFilters,
        String timeout,
        LdapAttributes attributes,
        String rootCa,
        ProviderOptions providerOptions) {

    public static Builder builder() {
        return new Builder();
    }

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

        public Builder idpRef(String idpRef) {
            this.idpRef = idpRef;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder servers(List<String> servers) {
            this.servers = servers;
            return this;
        }

        public Builder startTls(Boolean startTls) {
            this.startTls = startTls;
            return this;
        }

        public Builder baseDn(String baseDn) {
            this.baseDn = baseDn;
            return this;
        }

        public Builder bindDn(String bindDn) {
            this.bindDn = bindDn;
            return this;
        }

        public Builder bindPassword(String bindPassword) {
            this.bindPassword = bindPassword;
            return this;
        }

        public Builder userBase(String userBase) {
            this.userBase = userBase;
            return this;
        }

        public Builder userObjectClasses(List<String> userObjectClasses) {
            this.userObjectClasses = userObjectClasses;
            return this;
        }

        public Builder userFilters(List<String> userFilters) {
            this.userFilters = userFilters;
            return this;
        }

        public Builder timeout(String timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder attributes(LdapAttributes attributes) {
            this.attributes = attributes;
            return this;
        }

        public Builder rootCa(String rootCa) {
            this.rootCa = rootCa;
            return this;
        }

        public Builder providerOptions(ProviderOptions providerOptions) {
            this.providerOptions = providerOptions;
            return this;
        }

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
