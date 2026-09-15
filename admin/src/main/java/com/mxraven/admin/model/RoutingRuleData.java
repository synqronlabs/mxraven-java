package com.mxraven.admin.model;

/**
 * Wire representation of a listener routing rule. Prefer the {@link RoutingRule}
 * entity.
 */
public record RoutingRuleData(
        String id,
        String listenerId,
        int priority,
        String expressionText,
        RoutingRuleAction action,
        boolean isActive) {
}
