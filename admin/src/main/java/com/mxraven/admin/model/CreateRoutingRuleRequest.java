package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for
 * <code>POST /v2/tenants/{slug}/listeners/{listener_id}/rules</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CreateRoutingRuleRequest {
    private final int priority;
    private final String expressionText;
    private final RoutingRuleAction action;

    /** evaluation order; lower values run first */
    public int priority() {
        return priority;
    }

    /** match expression */
    public String expressionText() {
        return expressionText;
    }

    /** action performed on a match */
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
        CreateRoutingRuleRequest that = (CreateRoutingRuleRequest) o;
        return this.priority == that.priority
                && Objects.equals(this.expressionText, that.expressionText)
                && Objects.equals(this.action, that.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.priority, this.expressionText, this.action);
    }

    @Override
    public String toString() {
        return "CreateRoutingRuleRequest[" + "priority=" + this.priority + ", " + "expressionText=" + this.expressionText + ", " + "action=" + this.action + "]";
    }

    /**
     * Creates a new CreateRoutingRuleRequest.
     *
     * @param priority evaluation order; lower values run first
     * @param expressionText match expression
     * @param action action performed on a match
     */
    @JsonCreator
    public CreateRoutingRuleRequest(int priority, String expressionText, RoutingRuleAction action) {
        this.priority = priority;
        this.expressionText = expressionText;
        this.action = action;
    }

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
            if (expressionText == null || Java8.isBlank(expressionText)) {
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
