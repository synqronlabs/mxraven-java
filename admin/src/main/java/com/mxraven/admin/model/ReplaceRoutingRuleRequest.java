package com.mxraven.admin.model;

/**
 * Request body for
 * {@code PUT /v2/tenants/{slug}/listeners/{listener_id}/rules/{rule_id}}. This is
 * a full replacement of the writable rule definition; priority is unchanged.
 */
public record ReplaceRoutingRuleRequest(
        String expressionText,
        RoutingRuleAction action) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String expressionText;
        private RoutingRuleAction action;

        public Builder expressionText(String expressionText) {
            this.expressionText = expressionText;
            return this;
        }

        public Builder action(RoutingRuleAction action) {
            this.action = action;
            return this;
        }

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
