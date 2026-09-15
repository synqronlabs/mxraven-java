package com.mxraven.admin.model;

/**
 * Request body for
 * {@code PUT /v2/tenants/{slug}/inbound-routes/{route_id}}.
 */
public record UpdateInboundRouteRequest(
        String mtaListenerId,
        String domainName) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String mtaListenerId;
        private String domainName;

        public Builder mtaListenerId(String mtaListenerId) {
            this.mtaListenerId = mtaListenerId;
            return this;
        }

        public Builder domainName(String domainName) {
            this.domainName = domainName;
            return this;
        }

        public UpdateInboundRouteRequest build() {
            return new UpdateInboundRouteRequest(mtaListenerId, domainName);
        }
    }
}
