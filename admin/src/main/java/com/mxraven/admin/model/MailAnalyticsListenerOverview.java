package com.mxraven.admin.model;

import java.util.List;

/**
 * Fixed per-listener mail-analytics catalog with a zero-filled UTC time series.
 *
 * <p>The interval is start-inclusive and end-exclusive and may span at most 31
 * days; the server selects hourly buckets for intervals up to 72 hours and
 * daily buckets for longer intervals. Time-series buckets exactly partition the
 * interval and summary values equal the corresponding time-series totals.
 *
 * @param startAt inclusive UTC start of the analytics interval
 * @param endAt exclusive UTC end of the analytics interval
 * @param bucketGranularity bucket granularity selected by the server
 * @param queriedAt UTC timestamp when the query was evaluated
 * @param eventualConsistencyNotice note about eventual consistency of the data
 * @param listener identity of the reported listener
 * @param summary summary metrics for the interval
 * @param timeSeries zero-filled buckets ordered oldest first
 */
public record MailAnalyticsListenerOverview(
        String startAt,
        String endAt,
        MailAnalyticsBucketGranularity bucketGranularity,
        String queriedAt,
        String eventualConsistencyNotice,
        MailAnalyticsListenerIdentity listener,
        MailAnalyticsListenerOverviewMetrics summary,
        List<MailAnalyticsListenerOverviewTimePoint> timeSeries) {
}
