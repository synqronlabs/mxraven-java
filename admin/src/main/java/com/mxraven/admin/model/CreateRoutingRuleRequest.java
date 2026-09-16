package com.mxraven.admin.model;

/**
 * Request body for
 * {@code POST /v2/tenants/{slug}/listeners/{listener_id}/rules}.
 *
 * @param priority       evaluation order; lower values run first
 * @param expressionText match expression
 * @param action         action performed on a match
 */
public record CreateRoutingRuleRequest(
        int priority,
        String expressionText,
        RoutingRuleAction action) {

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds a {@link CreateRoutingRuleRequest}. */
    public static final class Builder {
        private int priority;
        private String expressionText;
        private RoutingRuleAction action;

        /**
         * Sets the evaluation order.
         *
         * @param priority evaluation order; lower values run first
         * @return this builder
         */
        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        /**
         * Sets the match expression.
         *
         * @param expressionText match expression
         * @return this builder
         */
        public Builder expressionText(String expressionText) {
            this.expressionText = expressionText;
            return this;
        }

        /**
         * Sets the action performed on a match.
         *
         * @param action match action
         * @return this builder
         */
        public Builder action(RoutingRuleAction action) {
            this.action = action;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return new request
         * @throws IllegalArgumentException when a field is missing or invalid
         */
        public CreateRoutingRuleRequest build() {
            if (priority < 1) {
                throw new IllegalArgumentException("priority must be greater than 0");
            }
            if (expressionText == null || expressionText.isBlank()) {
                throw new IllegalArgumentException("expression_text is required");
            }
            if (expressionText.codePointCount(0, expressionText.length()) > 4096) {
                throw new IllegalArgumentException("expression_text must be 4096 characters or fewer");
            }
            if (action == null) {
                throw new IllegalArgumentException("action is required");
            }
            return new CreateRoutingRuleRequest(priority, expressionText, action);
        }
    }
}
