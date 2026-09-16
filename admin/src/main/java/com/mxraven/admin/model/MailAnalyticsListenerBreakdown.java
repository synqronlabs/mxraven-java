package com.mxraven.admin.model;

import java.util.List;

/**
 * Bounded listener breakdown of egress task volume for an overview interval.
 *
 * @param limit maximum ranked listeners requested
 * @param totalEgressTasks total egress tasks across all listeners
 * @param items ranked listener items
 * @param otherEgressTasks egress tasks not covered by the returned items
 */
public record MailAnalyticsListenerBreakdown(
        int limit,
        long totalEgressTasks,
        List<MailAnalyticsListenerBreakdownItem> items,
        long otherEgressTasks) {
}
