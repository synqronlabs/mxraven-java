package com.mxraven.admin.model;

/**
 * Request body for
 * {@code PUT /v2/tenants/{slug}/inbound-routes/{route_id}}.
 *
 * @param mtaListenerId identifier of the MTA listener that routes inbound mail
 * @param domainName inbound domain name
 */
public record UpdateInboundRouteRequest(
        String mtaListenerId,
        String domainName) {

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link UpdateInboundRouteRequest} instances. */
    public static final class Builder {
        private String mtaListenerId;
        private String domainName;

        /**
         * Sets the MTA listener that routes inbound mail.
         *
         * @param mtaListenerId MTA listener identifier
         * @return this builder
         */
        public Builder mtaListenerId(String mtaListenerId) {
            this.mtaListenerId = mtaListenerId;
            return this;
        }

        /**
         * Sets the inbound domain name.
         *
         * @param domainName inbound domain name
         * @return this builder
         */
        public Builder domainName(String domainName) {
            this.domainName = domainName;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return the update request
         */
        public UpdateInboundRouteRequest build() {
            return new UpdateInboundRouteRequest(mtaListenerId, domainName);
        }
    }
}
