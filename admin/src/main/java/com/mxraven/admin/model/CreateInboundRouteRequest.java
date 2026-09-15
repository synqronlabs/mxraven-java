package com.mxraven.admin.model;

/**
 * Request body for {@code POST /v2/tenants/{slug}/inbound-routes}.
 *
 * @param onboardDomainIfMissing when true, atomically onboard an unclaimed
 *                               inbound-only domain before creating the route
 */
public record CreateInboundRouteRequest(
        String mtaListenerId,
        String domainName,
        Boolean onboardDomainIfMissing) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String mtaListenerId;
        private String domainName;
        private Boolean onboardDomainIfMissing;

        public Builder mtaListenerId(String mtaListenerId) {
            this.mtaListenerId = mtaListenerId;
            return this;
        }

        public Builder domainName(String domainName) {
            this.domainName = domainName;
            return this;
        }

        public Builder onboardDomainIfMissing(Boolean onboardDomainIfMissing) {
            this.onboardDomainIfMissing = onboardDomainIfMissing;
            return this;
        }

        public CreateInboundRouteRequest build() {
            return new CreateInboundRouteRequest(mtaListenerId, domainName, onboardDomainIfMissing);
        }
    }
}
