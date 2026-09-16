package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for
 * <code>PUT /v2/tenants/{slug}/listeners/{listener_id}/rules/{rule_id}/priority</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ReorderRoutingRuleRequest {
    private final int priority;

    /** new evaluation order; lower values run first */
    public int priority() {
        return priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReorderRoutingRuleRequest that = (ReorderRoutingRuleRequest) o;
        return this.priority == that.priority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.priority);
    }

    @Override
    public String toString() {
        return "ReorderRoutingRuleRequest[" + "priority=" + this.priority + "]";
    }

    /**
     * Creates a new ReorderRoutingRuleRequest.
     *
     * @param priority new evaluation order; lower values run first
     */
    @JsonCreator
    public ReorderRoutingRuleRequest(int priority) {
        this.priority = priority;
    }

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
