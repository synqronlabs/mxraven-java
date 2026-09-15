package com.mxraven.admin.model;

/**
 * Request body for
 * {@code PUT /v2/tenants/{slug}/listeners/{listener_id}/rules/{rule_id}/active}.
 */
public record SetRoutingRuleActiveRequest(boolean isActive) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private boolean isActive;

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public SetRoutingRuleActiveRequest build() {
            return new SetRoutingRuleActiveRequest(isActive);
        }
    }
}
