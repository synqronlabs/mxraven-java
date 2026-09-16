package com.mxraven.admin.model;

import java.util.List;

/**
 * Observed latency metrics across first attempt, successful delivery, and
 * terminal outcomes.
 *
 * @param firstAttempt latency of first delivery attempts
 * @param successfulDelivery latency of successful deliveries
 * @param terminalOutcome latency of terminal outcomes
 * @param terminalByOutcome terminal latency per outcome
 */
public record MailAnalyticsLatencySet(
        MailAnalyticsLatencyMetric firstAttempt,
        MailAnalyticsLatencyMetric successfulDelivery,
        MailAnalyticsLatencyMetric terminalOutcome,
        List<MailAnalyticsOutcomeLatency> terminalByOutcome) {
}
