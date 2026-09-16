package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for
 * <code>PUT /v2/tenants/{slug}/inbound-routes/{route_id}</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class UpdateInboundRouteRequest {
    private final String mtaListenerId;
    private final String domainName;

    /** identifier of the MTA listener that routes inbound mail */
    public String mtaListenerId() {
        return mtaListenerId;
    }

    /** inbound domain name */
    public String domainName() {
        return domainName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpdateInboundRouteRequest that = (UpdateInboundRouteRequest) o;
        return Objects.equals(this.mtaListenerId, that.mtaListenerId)
                && Objects.equals(this.domainName, that.domainName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.mtaListenerId, this.domainName);
    }

    @Override
    public String toString() {
        return "UpdateInboundRouteRequest[" + "mtaListenerId=" + this.mtaListenerId + ", " + "domainName=" + this.domainName + "]";
    }

    /**
     * Creates a new UpdateInboundRouteRequest.
     *
     * @param mtaListenerId identifier of the MTA listener that routes inbound mail
     * @param domainName inbound domain name
     */
    @JsonCreator
    public UpdateInboundRouteRequest(String mtaListenerId, String domainName) {
        this.mtaListenerId = mtaListenerId;
        this.domainName = domainName;
    }

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
