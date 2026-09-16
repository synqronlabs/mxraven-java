package com.mxraven.admin.model;

/**
 * Wire representation of a listener routing rule. Prefer the {@link RoutingRule}
 * entity.
 *
 * @param id             identifier of the rule
 * @param listenerId     identifier of the owning listener
 * @param priority       evaluation order; lower values run first
 * @param expressionText rule expression text
 * @param action         action applied when the expression matches
 * @param isActive       whether the rule is enabled
 */
public record RoutingRuleData(
        String id,
        String listenerId,
        int priority,
        String expressionText,
        RoutingRuleAction action,
        boolean isActive) {
}
