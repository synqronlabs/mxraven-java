package com.mxraven.admin.model;

/**
 * Request body for {@code POST /v2/tenants/{slug}/inbound-routes}.
 *
 * @param mtaListenerId          listener that receives the routed mail
 * @param domainName             inbound domain name
 * @param onboardDomainIfMissing when true, atomically onboard an unclaimed
 *                               inbound-only domain before creating the route
 */
public record CreateInboundRouteRequest(
        String mtaListenerId,
        String domainName,
        Boolean onboardDomainIfMissing) {

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
