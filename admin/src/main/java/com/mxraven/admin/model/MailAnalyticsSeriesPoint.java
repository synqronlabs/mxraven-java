package com.mxraven.admin.model;

/**
 * One bucket value within a series item.
 *
 * @param bucketStart start of the bucket
 * @param bucketEnd   end of the bucket
 * @param value       aggregated value for the bucket
 */
public record MailAnalyticsSeriesPoint(
        String bucketStart,
        String bucketEnd,
        long value) {
}
