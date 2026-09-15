package com.mxraven.admin.model;

/**
 * Terminal latency for a single terminal outcome.
 */
public record MailAnalyticsOutcomeLatency(
        MailAnalyticsOutcome outcome,
        MailAnalyticsLatencyMetric latency) {
}
