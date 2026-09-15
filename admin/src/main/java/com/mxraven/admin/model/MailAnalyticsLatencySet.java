package com.mxraven.admin.model;

import java.util.List;

/**
 * Observed latency metrics across first attempt, successful delivery, and
 * terminal outcomes.
 */
public record MailAnalyticsLatencySet(
        MailAnalyticsLatencyMetric firstAttempt,
        MailAnalyticsLatencyMetric successfulDelivery,
        MailAnalyticsLatencyMetric terminalOutcome,
        List<MailAnalyticsOutcomeLatency> terminalByOutcome) {
}
