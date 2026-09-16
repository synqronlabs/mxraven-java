package com.mxraven.admin.model;

/**
 * SMTP response class counts from SMTP-channel lifecycle facts with a non-zero
 * SMTP status code.
 *
 * @param successful       count of 2xx SMTP responses
 * @param temporaryFailure count of 4xx SMTP responses
 * @param permanentFailure count of 5xx SMTP responses
 */
public record MailAnalyticsSMTPResponses(
        long successful,
        long temporaryFailure,
        long permanentFailure) {
}
