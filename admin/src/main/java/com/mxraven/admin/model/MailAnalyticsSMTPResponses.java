package com.mxraven.admin.model;

/**
 * SMTP response class counts from SMTP-channel lifecycle facts with a non-zero
 * SMTP status code.
 */
public record MailAnalyticsSMTPResponses(
        long successful,
        long temporaryFailure,
        long permanentFailure) {
}
