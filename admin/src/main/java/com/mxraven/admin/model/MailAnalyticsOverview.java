package com.mxraven.admin.model;

import java.util.List;

/**
 * Tenant mail analytics overview: summary metrics, a zero-filled UTC time
 * series, and a bounded listener breakdown for the requested interval.
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
