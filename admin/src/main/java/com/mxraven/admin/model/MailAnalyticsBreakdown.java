package com.mxraven.admin.model;

import java.util.List;

/**
 * A bounded, ranked analytics breakdown grouping one additive metric by one
 * ledger dimension.
 *
 * @param startAt inclusive UTC start of the interval
 * @param endAt exclusive UTC end of the interval
 * @param queriedAt UTC timestamp when the query was evaluated
 * @param eventualConsistencyNotice note about eventual consistency of the data
 * @param metric additive metric being grouped
 * @param dimension ledger dimension used to group the metric
 * @param limit maximum ranked items requested
 * @param total total metric value across all groups
 * @param items ranked breakdown items
 * @param other metric value not covered by the returned items
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
