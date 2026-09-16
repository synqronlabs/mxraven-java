package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Wire representation of a listener routing rule. Prefer the {@link RoutingRule}
 * entity.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class RoutingRuleData {
    private final String id;
    private final String listenerId;
    private final int priority;
    private final String expressionText;
    private final RoutingRuleAction action;
    private final boolean isActive;

    /** identifier of the rule */
    public String id() {
        return id;
    }

    /** identifier of the owning listener */
    public String listenerId() {
        return listenerId;
    }

    /** evaluation order; lower values run first */
    public int priority() {
        return priority;
    }

    /** rule expression text */
    public String expressionText() {
        return expressionText;
    }

    /** action applied when the expression matches */
    public RoutingRuleAction action() {
        return action;
    }

    /** whether the rule is enabled */
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
        RoutingRuleData that = (RoutingRuleData) o;
        return Objects.equals(this.id, that.id)
                && Objects.equals(this.listenerId, that.listenerId)
                && this.priority == that.priority
                && Objects.equals(this.expressionText, that.expressionText)
                && Objects.equals(this.action, that.action)
                && this.isActive == that.isActive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.listenerId, this.priority, this.expressionText, this.action, this.isActive);
    }

    @Override
    public String toString() {
        return "RoutingRuleData[" + "id=" + this.id + ", " + "listenerId=" + this.listenerId + ", " + "priority=" + this.priority + ", " + "expressionText=" + this.expressionText + ", " + "action=" + this.action + ", " + "isActive=" + this.isActive + "]";
    }

    /**
     * Creates a new RoutingRuleData.
     *
     * @param id identifier of the rule
     * @param listenerId identifier of the owning listener
     * @param priority evaluation order; lower values run first
     * @param expressionText rule expression text
     * @param action action applied when the expression matches
     * @param isActive whether the rule is enabled
     */
    @JsonCreator
    public RoutingRuleData(String id, String listenerId, int priority, String expressionText, RoutingRuleAction action, boolean isActive) {
        this.id = id;
        this.listenerId = listenerId;
        this.priority = priority;
        this.expressionText = expressionText;
        this.action = action;
        this.isActive = isActive;
    }
}
