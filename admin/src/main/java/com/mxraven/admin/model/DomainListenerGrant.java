package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * A listener authorized to send from a domain.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class DomainListenerGrant {
    private final String listenerId;
    private final String listenerDisplayName;
    private final ListenerType listenerType;
    private final StreamType streamType;
    private final DomainStatus domainStatus;
    private final SendingDomainSubdomainScope subdomainScope;

    /** identifier of the granted listener */
    public String listenerId() {
        return listenerId;
    }

    /** human-readable listener name */
    public String listenerDisplayName() {
        return listenerDisplayName;
    }

    /** always {@code submission} in v2 */
    public ListenerType listenerType() {
        return listenerType;
    }

    /** {@code transactional} or {@code broadcast} */
    public StreamType streamType() {
        return streamType;
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
        DomainListenerGrant that = (DomainListenerGrant) o;
        return Objects.equals(this.listenerId, that.listenerId)
                && Objects.equals(this.listenerDisplayName, that.listenerDisplayName)
                && Objects.equals(this.listenerType, that.listenerType)
                && Objects.equals(this.streamType, that.streamType)
                && Objects.equals(this.domainStatus, that.domainStatus)
                && Objects.equals(this.subdomainScope, that.subdomainScope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.listenerId, this.listenerDisplayName, this.listenerType, this.streamType, this.domainStatus, this.subdomainScope);
    }

    @Override
    public String toString() {
        return "DomainListenerGrant[" + "listenerId=" + this.listenerId + ", " + "listenerDisplayName=" + this.listenerDisplayName + ", " + "listenerType=" + this.listenerType + ", " + "streamType=" + this.streamType + ", " + "domainStatus=" + this.domainStatus + ", " + "subdomainScope=" + this.subdomainScope + "]";
    }

    /**
     * Creates a new DomainListenerGrant.
     *
     * @param listenerId identifier of the granted listener
     * @param listenerDisplayName human-readable listener name
     * @param listenerType always {@code submission} in v2
     * @param streamType {@code transactional} or {@code broadcast}
     * @param domainStatus {@code pending}, {@code verified}, or {@code suspended}
     * @param subdomainScope {@code exact} or {@code include_subdomains}
     */
    @JsonCreator
    public DomainListenerGrant(String listenerId, String listenerDisplayName, ListenerType listenerType, StreamType streamType, DomainStatus domainStatus, SendingDomainSubdomainScope subdomainScope) {
        this.listenerId = listenerId;
        this.listenerDisplayName = listenerDisplayName;
        this.listenerType = listenerType;
        this.streamType = streamType;
        this.domainStatus = domainStatus;
        this.subdomainScope = subdomainScope;
    }
}
