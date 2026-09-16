package com.mxraven.admin.model;

/**
 * A latency statistic with sample count and percentiles.
 *
 * <p>Percentiles are null when there are no samples.
 *
 * @param sampleCount number of samples
 * @param p50Ms 50th-percentile latency in milliseconds, or {@code null} when there are no samples
 * @param p90Ms 90th-percentile latency in milliseconds, or {@code null} when there are no samples
 * @param p95Ms 95th-percentile latency in milliseconds, or {@code null} when there are no samples
 * @param p99Ms 99th-percentile latency in milliseconds, or {@code null} when there are no samples
 */
public record MailAnalyticsLatencyStatistic(
        long sampleCount,
        Double p50Ms,
        Double p90Ms,
        Double p95Ms,
        Double p99Ms) {
}
