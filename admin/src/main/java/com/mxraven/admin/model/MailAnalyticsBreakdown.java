package com.mxraven.admin.model;

import java.util.List;

/**
 * A bounded, ranked analytics breakdown grouping one additive metric by one
 * ledger dimension.
 */
public record MailAnalyticsBreakdown(
        String startAt,
        String endAt,
        String queriedAt,
        String eventualConsistencyNotice,
        MailAnalyticsMetric metric,
        MailAnalyticsDimension dimension,
        int limit,
        long total,
        List<MailAnalyticsBreakdownItem> items,
        long other) {
}
