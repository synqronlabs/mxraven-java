package com.mxraven.admin.model;

/**
 * One domain grant in a sending-domain policy replacement.
 *
 * @param domainId       identifier of the granted domain
 * @param subdomainScope {@code exact} or {@code include_subdomains}
 */
public record SendingDomainPolicyGrantRequest(
        String domainId,
        SendingDomainSubdomainScope subdomainScope) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String domainId;
        private SendingDomainSubdomainScope subdomainScope;

        public Builder domainId(String domainId) {
            this.domainId = domainId;
            return this;
        }

        public Builder subdomainScope(SendingDomainSubdomainScope subdomainScope) {
            this.subdomainScope = subdomainScope;
            return this;
        }

        public SendingDomainPolicyGrantRequest build() {
            return new SendingDomainPolicyGrantRequest(domainId, subdomainScope);
        }
    }
}
