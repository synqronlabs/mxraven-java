package com.mxraven.admin.model;

/**
 * Request body for
 * {@code PUT /v2/tenants/{slug}/listeners/{listener_id}/rules/{rule_id}}. This is
 * a full replacement of the writable rule definition; priority is unchanged.
 *
 * @param expressionText rule expression text
 * @param action         action applied when the expression matches
 */
public record ReplaceRoutingRuleRequest(
        String expressionText,
        RoutingRuleAction action) {

    /**
     * Creates a new builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds a {@link ReplaceRoutingRuleRequest}. */
    public static final class Builder {
        private String expressionText;
        private RoutingRuleAction action;

        /**
         * Sets the rule expression text.
         *
         * @param expressionText rule expression text
         * @return this builder
         */
        public Builder expressionText(String expressionText) {
            this.expressionText = expressionText;
            return this;
        }

        /**
         * Sets the action.
         *
         * @param action action applied when the expression matches
         * @return this builder
         */
        public Builder action(RoutingRuleAction action) {
            this.action = action;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return a new request
         * @throws IllegalArgumentException if the expression text or action is missing or invalid
         */
        public ReplaceRoutingRuleRequest build() {
            if (expressionText == null || expressionText.isBlank()) {
                throw new IllegalArgumentException("expression_text is required");
            }
            if (expressionText.codePointCount(0, expressionText.length()) > 4096) {
                throw new IllegalArgumentException("expression_text must be 4096 characters or fewer");
            }
            if (action == null) {
                throw new IllegalArgumentException("action is required");
            }
            return new ReplaceRoutingRuleRequest(expressionText, action);
        }
    }
}
