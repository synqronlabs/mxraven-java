package com.mxraven.admin.model;

/**
 * Request body for
 * {@code PUT /v2/tenants/{slug}/listeners/{listener_id}/rules/{rule_id}/active}.
 *
 * @param isActive whether the rule is active
 */
public record SetRoutingRuleActiveRequest(boolean isActive) {

    /**
     * Creates a new builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link SetRoutingRuleActiveRequest} instances. */
    public static final class Builder {
        private boolean isActive;

        /**
         * Sets whether the rule is active.
         *
         * @param isActive whether the rule is active
         * @return this builder
         */
        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return a new request
         */
        public SetRoutingRuleActiveRequest build() {
            return new SetRoutingRuleActiveRequest(isActive);
        }
    }
}
