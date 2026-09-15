package com.mxraven.admin.model;

import java.util.List;

/**
 * A latency metric with sample count, percentiles, and a count distribution.
 *
 * <p>Percentiles are null when there are no samples.
 */
public record MailAnalyticsLatencyMetric(
        long sampleCount,
        Double p50Ms,
        Double p90Ms,
        Double p95Ms,
        Double p99Ms,
        List<MailAnalyticsCountBin> distribution) {
}
