package com.mxraven.admin.model;

import java.util.List;

/**
 * Bounded listener breakdown of egress task volume for an overview interval.
 */
public record MailAnalyticsListenerBreakdown(
        int limit,
        long totalEgressTasks,
        List<MailAnalyticsListenerBreakdownItem> items,
        long otherEgressTasks) {
}
