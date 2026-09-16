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

    /**
     * Creates a new builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link SendingDomainPolicyGrantRequest} instances. */
    public static final class Builder {
        private String domainId;
        private SendingDomainSubdomainScope subdomainScope;

        /**
         * Sets the granted domain identifier.
         *
         * @param domainId identifier of the granted domain
         * @return this builder
         */
        public Builder domainId(String domainId) {
            this.domainId = domainId;
            return this;
        }

        /**
         * Sets the subdomain scope.
         *
         * @param subdomainScope {@code exact} or {@code include_subdomains}
         * @return this builder
         */
        public Builder subdomainScope(SendingDomainSubdomainScope subdomainScope) {
            this.subdomainScope = subdomainScope;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return a new request
         */
        public SendingDomainPolicyGrantRequest build() {
            return new SendingDomainPolicyGrantRequest(domainId, subdomainScope);
        }
    }
}
