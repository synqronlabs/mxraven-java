package com.mxraven.admin.model;

/**
 * One zero-filled UTC time-series bucket of a listener overview.
 *
 * @param bucketStart inclusive UTC start of the bucket
 * @param bucketEnd exclusive UTC end of the bucket
 * @param metrics metrics for the bucket
 */
public record MailAnalyticsListenerOverviewTimePoint(
        String bucketStart,
        String bucketEnd,
        MailAnalyticsListenerOverviewMetrics metrics) {
}
