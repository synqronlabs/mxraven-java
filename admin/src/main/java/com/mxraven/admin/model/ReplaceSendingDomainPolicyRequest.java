package com.mxraven.admin.model;

import java.util.List;

/**
 * Request body for
 * {@code PUT /v2/tenants/{slug}/listeners/{listener_id}/sending-domain-policy}.
 * This atomically replaces the listener's complete set of domain grants.
 *
 * @param grants complete set of domain grants
 */
public record ReplaceSendingDomainPolicyRequest(
        List<SendingDomainPolicyGrantRequest> grants) {

    /**
     * Creates a new builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds a {@link ReplaceSendingDomainPolicyRequest}. */
    public static final class Builder {
        private List<SendingDomainPolicyGrantRequest> grants;

        /**
         * Sets the complete set of domain grants.
         *
         * @param grants complete set of domain grants
         * @return this builder
         */
        public Builder grants(List<SendingDomainPolicyGrantRequest> grants) {
            this.grants = grants;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return a new request
         */
        public ReplaceSendingDomainPolicyRequest build() {
            return new ReplaceSendingDomainPolicyRequest(grants);
        }
    }
}
