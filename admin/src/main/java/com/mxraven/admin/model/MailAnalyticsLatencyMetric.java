package com.mxraven.admin.model;

import java.util.List;

/**
 * A latency metric with sample count, percentiles, and a count distribution.
 *
 * <p>Percentiles are null when there are no samples.
 *
 * @param sampleCount number of samples
 * @param p50Ms 50th-percentile latency in milliseconds, or {@code null} when there are no samples
 * @param p90Ms 90th-percentile latency in milliseconds, or {@code null} when there are no samples
 * @param p95Ms 95th-percentile latency in milliseconds, or {@code null} when there are no samples
 * @param p99Ms 99th-percentile latency in milliseconds, or {@code null} when there are no samples
 * @param distribution count distribution of the samples
 */
public record MailAnalyticsLatencyMetric(
        long sampleCount,
        Double p50Ms,
        Double p90Ms,
        Double p95Ms,
        Double p99Ms,
        List<MailAnalyticsCountBin> distribution) {
}
