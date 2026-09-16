package com.mxraven.admin.model;

/**
 * One zero-filled overview time-series bucket.
 *
 * @param bucketStart start of the bucket
 * @param bucketEnd   end of the bucket
 * @param metrics     overview metrics for the bucket
 */
public record MailAnalyticsTimePoint(
        String bucketStart,
        String bucketEnd,
        MailAnalyticsMetrics metrics) {
}
