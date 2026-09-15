package com.mxraven.admin.model;

/**
 * Feedback fact counts derived from SMTP feedback, excluding forwarding audit
 * events.
 */
public record MailAnalyticsFeedback(
        long dsn,
        long arf,
        long oneClickUnsubscribe,
        long tenantTraining) {
}
