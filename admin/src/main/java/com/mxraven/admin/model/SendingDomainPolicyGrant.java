package com.mxraven.admin.model;

/**
 * A resolved domain grant on a listener's sending-domain policy.
 *
 * @param domainId       identifier of the granted domain
 * @param domainName     human-readable domain name
 * @param domainStatus   {@code pending}, {@code verified}, or {@code suspended}
 * @param subdomainScope {@code exact} or {@code include_subdomains}
 */
public record SendingDomainPolicyGrant(
        String domainId,
        String domainName,
        DomainStatus domainStatus,
        SendingDomainSubdomainScope subdomainScope) {
}
