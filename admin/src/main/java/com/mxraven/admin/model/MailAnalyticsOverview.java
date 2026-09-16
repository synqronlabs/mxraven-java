package com.mxraven.admin.model;

import java.util.List;

/**
 * Tenant mail analytics overview: summary metrics, a zero-filled UTC time
 * series, and a bounded listener breakdown for the requested interval.
 *
 * @param startAt inclusive UTC start of the interval
 * @param endAt exclusive UTC end of the interval
 * @param bucketGranularity granularity of the time-series buckets
 * @param queriedAt UTC timestamp when the query was evaluated
 * @param eventualConsistencyNotice note about eventual consistency of the data
 * @param summary summary metrics for the interval
 * @param timeSeries zero-filled UTC time-series points
 * @param listenerBreakdown bounded listener breakdown
 */
public record MailAnalyticsOverview(
        String startAt,
        String endAt,
        MailAnalyticsBucketGranularity bucketGranularity,
        String queriedAt,
        String eventualConsistencyNotice,
        MailAnalyticsMetrics summary,
        List<MailAnalyticsTimePoint> timeSeries,
        MailAnalyticsListenerBreakdown listenerBreakdown) {
}
