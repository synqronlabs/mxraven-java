package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for <code>POST /v2/tenants/{slug}/inbound-routes</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CreateInboundRouteRequest {
    private final String mtaListenerId;
    private final String domainName;
    private final Boolean onboardDomainIfMissing;

    /** listener that receives the routed mail */
    public String mtaListenerId() {
        return mtaListenerId;
    }

    /** inbound domain name */
    public String domainName() {
        return domainName;
    }

    /** when true, atomically onboard an unclaimed inbound-only domain before creating the route */
    public Boolean onboardDomainIfMissing() {
        return onboardDomainIfMissing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateInboundRouteRequest that = (CreateInboundRouteRequest) o;
        return Objects.equals(this.mtaListenerId, that.mtaListenerId)
                && Objects.equals(this.domainName, that.domainName)
                && Objects.equals(this.onboardDomainIfMissing, that.onboardDomainIfMissing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.mtaListenerId, this.domainName, this.onboardDomainIfMissing);
    }

    @Override
    public String toString() {
        return "CreateInboundRouteRequest[" + "mtaListenerId=" + this.mtaListenerId + ", " + "domainName=" + this.domainName + ", " + "onboardDomainIfMissing=" + this.onboardDomainIfMissing + "]";
    }

    /**
     * Creates a new CreateInboundRouteRequest.
     *
     * @param mtaListenerId listener that receives the routed mail
     * @param domainName inbound domain name
     * @param onboardDomainIfMissing when true, atomically onboard an unclaimed inbound-only domain before creating the route
     */
    @JsonCreator
    public CreateInboundRouteRequest(String mtaListenerId, String domainName, Boolean onboardDomainIfMissing) {
        this.mtaListenerId = mtaListenerId;
        this.domainName = domainName;
        this.onboardDomainIfMissing = onboardDomainIfMissing;
    }

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds a {@link CreateInboundRouteRequest}. */
    public static final class Builder {
        private String mtaListenerId;
        private String domainName;
        private Boolean onboardDomainIfMissing;

        /**
         * Sets the listener that receives the routed mail.
         *
         * @param mtaListenerId listener identifier
         * @return this builder
         */
        public Builder mtaListenerId(String mtaListenerId) {
            this.mtaListenerId = mtaListenerId;
            return this;
        }

        /**
         * Sets the inbound domain name.
         *
         * @param domainName domain name
         * @return this builder
         */
        public Builder domainName(String domainName) {
            this.domainName = domainName;
            return this;
        }

        /**
         * Sets whether to onboard an unclaimed inbound-only domain.
         *
         * @param onboardDomainIfMissing onboard flag
         * @return this builder
         */
        public Builder onboardDomainIfMissing(Boolean onboardDomainIfMissing) {
            this.onboardDomainIfMissing = onboardDomainIfMissing;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return new request
         */
        public CreateInboundRouteRequest build() {
            return new CreateInboundRouteRequest(mtaListenerId, domainName, onboardDomainIfMissing);
        }
    }
}
