package com.mxraven.admin.model;

/**
 * Request body for
 * {@code PUT /v2/tenants/{slug}/listeners/{listener_id}/rules/{rule_id}/priority}.
 *
 * @param priority new evaluation order; lower values run first
 */
public record ReorderRoutingRuleRequest(int priority) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private int priority;

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public ReorderRoutingRuleRequest build() {
            return new ReorderRoutingRuleRequest(priority);
        }
    }
}
