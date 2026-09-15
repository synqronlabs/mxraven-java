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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private int priority;
        private String expressionText;
        private RoutingRuleAction action;

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder expressionText(String expressionText) {
            this.expressionText = expressionText;
            return this;
        }

        public Builder action(RoutingRuleAction action) {
            this.action = action;
            return this;
        }

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
