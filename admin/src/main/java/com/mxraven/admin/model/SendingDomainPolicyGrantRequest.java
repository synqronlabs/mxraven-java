package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * One domain grant in a sending-domain policy replacement.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class SendingDomainPolicyGrantRequest {
    private final String domainId;
    private final SendingDomainSubdomainScope subdomainScope;

    /** identifier of the granted domain */
    public String domainId() {
        return domainId;
    }

    /** {@code exact} or {@code include_subdomains} */
    public SendingDomainSubdomainScope subdomainScope() {
        return subdomainScope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SendingDomainPolicyGrantRequest that = (SendingDomainPolicyGrantRequest) o;
        return Objects.equals(this.domainId, that.domainId)
                && Objects.equals(this.subdomainScope, that.subdomainScope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.domainId, this.subdomainScope);
    }

    @Override
    public String toString() {
        return "SendingDomainPolicyGrantRequest[" + "domainId=" + this.domainId + ", " + "subdomainScope=" + this.subdomainScope + "]";
    }

    /**
     * Creates a new SendingDomainPolicyGrantRequest.
     *
     * @param domainId identifier of the granted domain
     * @param subdomainScope {@code exact} or {@code include_subdomains}
     */
    @JsonCreator
    public SendingDomainPolicyGrantRequest(String domainId, SendingDomainSubdomainScope subdomainScope) {
        this.domainId = domainId;
        this.subdomainScope = subdomainScope;
    }

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
