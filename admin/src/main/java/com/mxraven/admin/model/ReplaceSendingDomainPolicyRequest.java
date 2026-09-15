package com.mxraven.admin.model;

import java.util.List;

/**
 * Request body for
 * {@code PUT /v2/tenants/{slug}/listeners/{listener_id}/sending-domain-policy}.
 * This atomically replaces the listener's complete set of domain grants.
 */
public record ReplaceSendingDomainPolicyRequest(
        List<SendingDomainPolicyGrantRequest> grants) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private List<SendingDomainPolicyGrantRequest> grants;

        public Builder grants(List<SendingDomainPolicyGrantRequest> grants) {
            this.grants = grants;
            return this;
        }

        public ReplaceSendingDomainPolicyRequest build() {
            return new ReplaceSendingDomainPolicyRequest(grants);
        }
    }
}
