package com.mxraven.admin.model;

/**
 * Summary metrics of one listener for the requested interval.
 *
 * @param acceptedMessages accepted message counts split by channel
 * @param egressTasks egress tasks created for the listener, including
 *                    protocol-generated tasks
 * @param deliverySuccessRate unique succeeded tasks over unique terminal tasks
 * @param smtpResponses SMTP response class counts
 */
public record MailAnalyticsListenerOverviewMetrics(
        MailAnalyticsAcceptedMessages acceptedMessages,
        long egressTasks,
        MailAnalyticsRate deliverySuccessRate,
        MailAnalyticsSMTPResponses smtpResponses) {
}
