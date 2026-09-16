package com.mxraven.admin.model;

/**
 * Size statistics for one UTC bucket. A zero sample count means the statistic
 * values are placeholders rather than observations.
 *
 * @param bucketStart  start of the UTC bucket
 * @param bucketEnd    end of the UTC bucket
 * @param sampleCount  number of message sizes observed
 * @param averageBytes mean message size in bytes
 * @param p50Bytes     50th percentile message size in bytes
 * @param p90Bytes     90th percentile message size in bytes
 * @param p95Bytes     95th percentile message size in bytes
 * @param p99Bytes     99th percentile message size in bytes
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
