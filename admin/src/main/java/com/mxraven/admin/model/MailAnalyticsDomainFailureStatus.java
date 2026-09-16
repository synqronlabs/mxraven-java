package com.mxraven.admin.model;

/**
 * Aggregated failure status for a recipient-domain lifecycle cohort.
 *
 * @param channel submission or MTA channel
 * @param smtpStatusCode SMTP status code
 * @param enhancedStatusCode enhanced status code
 * @param applicationStatusCode application status code
 * @param count number of failures with this status
 */
public record MailAnalyticsDomainFailureStatus(
        String channel,
        int smtpStatusCode,
        String enhancedStatusCode,
        long applicationStatusCode,
        long count) {
}
