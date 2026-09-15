package com.mxraven.admin.model;

/**
 * A latency statistic with sample count and percentiles.
 *
 * <p>Percentiles are null when there are no samples.
 */
public record MailAnalyticsLatencyStatistic(
        long sampleCount,
        Double p50Ms,
        Double p90Ms,
        Double p95Ms,
        Double p99Ms) {
}
