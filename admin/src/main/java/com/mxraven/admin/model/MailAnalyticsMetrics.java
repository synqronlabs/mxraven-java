package com.mxraven.admin.model;

/**
 * Fixed Phase 1 mail analytics summary metrics for an interval.
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
