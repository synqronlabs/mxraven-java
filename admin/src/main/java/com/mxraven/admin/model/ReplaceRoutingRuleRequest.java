package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for
 * <code>PUT /v2/tenants/{slug}/listeners/{listener_id}/rules/{rule_id}</code>. This is
 * a full replacement of the writable rule definition; priority is unchanged.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ReplaceRoutingRuleRequest {
    private final String expressionText;
    private final RoutingRuleAction action;

    /** rule expression text */
    public String expressionText() {
        return expressionText;
    }

    /** action applied when the expression matches */
    public RoutingRuleAction action() {
        return action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReplaceRoutingRuleRequest that = (ReplaceRoutingRuleRequest) o;
        return Objects.equals(this.expressionText, that.expressionText)
                && Objects.equals(this.action, that.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.expressionText, this.action);
    }

    @Override
    public String toString() {
        return "ReplaceRoutingRuleRequest[" + "expressionText=" + this.expressionText + ", " + "action=" + this.action + "]";
    }

    /**
     * Creates a new ReplaceRoutingRuleRequest.
     *
     * @param expressionText rule expression text
     * @param action action applied when the expression matches
     */
    @JsonCreator
    public ReplaceRoutingRuleRequest(String expressionText, RoutingRuleAction action) {
        this.expressionText = expressionText;
        this.action = action;
    }

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
            if (expressionText == null || Java8.isBlank(expressionText)) {
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
