package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * A resolved domain grant on a listener's sending-domain policy.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class SendingDomainPolicyGrant {
    private final String domainId;
    private final String domainName;
    private final DomainStatus domainStatus;
    private final SendingDomainSubdomainScope subdomainScope;

    /** identifier of the granted domain */
    public String domainId() {
        return domainId;
    }

    /** human-readable domain name */
    public String domainName() {
        return domainName;
    }

    /** {@code pending}, {@code verified}, or {@code suspended} */
    public DomainStatus domainStatus() {
        return domainStatus;
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
        SendingDomainPolicyGrant that = (SendingDomainPolicyGrant) o;
        return Objects.equals(this.domainId, that.domainId)
                && Objects.equals(this.domainName, that.domainName)
                && Objects.equals(this.domainStatus, that.domainStatus)
                && Objects.equals(this.subdomainScope, that.subdomainScope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.domainId, this.domainName, this.domainStatus, this.subdomainScope);
    }

    @Override
    public String toString() {
        return "SendingDomainPolicyGrant[" + "domainId=" + this.domainId + ", " + "domainName=" + this.domainName + ", " + "domainStatus=" + this.domainStatus + ", " + "subdomainScope=" + this.subdomainScope + "]";
    }

    /**
     * Creates a new SendingDomainPolicyGrant.
     *
     * @param domainId identifier of the granted domain
     * @param domainName human-readable domain name
     * @param domainStatus {@code pending}, {@code verified}, or {@code suspended}
     * @param subdomainScope {@code exact} or {@code include_subdomains}
     */
    @JsonCreator
    public SendingDomainPolicyGrant(String domainId, String domainName, DomainStatus domainStatus, SendingDomainSubdomainScope subdomainScope) {
        this.domainId = domainId;
        this.domainName = domainName;
        this.domainStatus = domainStatus;
        this.subdomainScope = subdomainScope;
    }
}
