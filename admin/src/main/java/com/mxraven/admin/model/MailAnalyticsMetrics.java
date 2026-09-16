package com.mxraven.admin.model;

/**
 * Fixed Phase 1 mail analytics summary metrics for an interval.
 *
 * @param acceptedMessages accepted message counts by channel
 * @param egressTasks number of egress tasks
 * @param dataBytes egress data bytes
 * @param billableUnits billable units
 * @param terminalOutcomes terminal outcome counts
 * @param deliverySuccessRate successful-delivery rate
 * @param smtpResponses SMTP response counts
 * @param feedback feedback fact counts
 * @param suppressionsApplied suppressions applied
 */
public record MailAnalyticsMetrics(
        MailAnalyticsAcceptedMessages acceptedMessages,
        long egressTasks,
        long dataBytes,
        long billableUnits,
        MailAnalyticsTerminalOutcomes terminalOutcomes,
        MailAnalyticsRate deliverySuccessRate,
        MailAnalyticsSMTPResponses smtpResponses,
        MailAnalyticsFeedback feedback,
        long suppressionsApplied) {
}
