package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for
 * <code>PUT /v2/tenants/{slug}/listeners/{listener_id}/rules/{rule_id}/active</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class SetRoutingRuleActiveRequest {
    private final boolean isActive;

    /** whether the rule is active */
    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SetRoutingRuleActiveRequest that = (SetRoutingRuleActiveRequest) o;
        return this.isActive == that.isActive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.isActive);
    }

    @Override
    public String toString() {
        return "SetRoutingRuleActiveRequest[" + "isActive=" + this.isActive + "]";
    }

    /**
     * Creates a new SetRoutingRuleActiveRequest.
     *
     * @param isActive whether the rule is active
     */
    @JsonCreator
    public SetRoutingRuleActiveRequest(boolean isActive) {
        this.isActive = isActive;
    }

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
