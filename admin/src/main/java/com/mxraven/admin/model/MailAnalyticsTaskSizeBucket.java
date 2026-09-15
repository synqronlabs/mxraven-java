package com.mxraven.admin.model;

/**
 * Size statistics for one UTC bucket. A zero sample count means the statistic
 * values are placeholders rather than observations.
 */
public record MailAnalyticsTaskSizeBucket(
        String bucketStart,
        String bucketEnd,
        long sampleCount,
        double averageBytes,
        double p50Bytes,
        double p90Bytes,
        double p95Bytes,
        double p99Bytes) {
}
