package com.mxraven.admin.model;

/**
 * One bucket value within a series item.
 */
public record MailAnalyticsSeriesPoint(
        String bucketStart,
        String bucketEnd,
        long value) {
}
