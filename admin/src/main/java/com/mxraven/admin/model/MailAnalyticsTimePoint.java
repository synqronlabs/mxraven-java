package com.mxraven.admin.model;

/**
 * One zero-filled overview time-series bucket.
 */
public record MailAnalyticsTimePoint(
        String bucketStart,
        String bucketEnd,
        MailAnalyticsMetrics metrics) {
}
