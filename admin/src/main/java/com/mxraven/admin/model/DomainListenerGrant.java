package com.mxraven.admin.model;

/**
 * A listener authorized to send from a domain.
 *
 * @param listenerId          identifier of the granted listener
 * @param listenerDisplayName human-readable listener name
 * @param listenerType        always {@code submission} in v2
 * @param streamType          {@code transactional} or {@code broadcast}
 * @param domainStatus        {@code pending}, {@code verified}, or {@code suspended}
 * @param subdomainScope      {@code exact} or {@code include_subdomains}
 */
public record DomainListenerGrant(
        String listenerId,
        String listenerDisplayName,
        ListenerType listenerType,
        StreamType streamType,
        DomainStatus domainStatus,
        SendingDomainSubdomainScope subdomainScope) {
}
