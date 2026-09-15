package com.mxraven.admin.model;

/**
 * Aggregated failure status for a recipient-domain lifecycle cohort.
 */
public record MailAnalyticsDomainFailureStatus(
        String channel,
        int smtpStatusCode,
        String enhancedStatusCode,
        long applicationStatusCode,
        long count) {
}
