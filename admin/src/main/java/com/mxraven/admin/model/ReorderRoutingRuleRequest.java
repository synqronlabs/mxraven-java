package com.mxraven.admin.model;

/**
 * Request body for
 * {@code PUT /v2/tenants/{slug}/listeners/{listener_id}/rules/{rule_id}/priority}.
 *
 * @param priority new evaluation order; lower values run first
 */
public record ReorderRoutingRuleRequest(int priority) {

    /**
     * Creates a new builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds a {@link ReorderRoutingRuleRequest}. */
    public static final class Builder {
        private int priority;

        /**
         * Sets the new priority.
         *
         * @param priority new evaluation order; lower values run first
         * @return this builder
         */
        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return a new request
         */
        public ReorderRoutingRuleRequest build() {
            return new ReorderRoutingRuleRequest(priority);
        }
    }
}
