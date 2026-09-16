package com.mxraven.admin.model;

/**
 * Terminal latency for a single terminal outcome.
 *
 * @param outcome terminal outcome
 * @param latency latency for the outcome
 */
public record MailAnalyticsOutcomeLatency(
        MailAnalyticsOutcome outcome,
        MailAnalyticsLatencyMetric latency) {
}
