package com.mxraven.admin.model;

/**
 * Feedback fact counts derived from SMTP feedback, excluding forwarding audit
 * events.
 *
 * @param dsn delivery status notification feedback count
 * @param arf abuse reporting format feedback count
 * @param oneClickUnsubscribe one-click unsubscribe feedback count
 * @param tenantTraining tenant-supplied training feedback count
 */
public record MailAnalyticsFeedback(
        long dsn,
        long arf,
        long oneClickUnsubscribe,
        long tenantTraining) {
}
