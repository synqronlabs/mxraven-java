package com.mxraven.admin.model;

/**
 * Accepted message counts split by submission and MTA channel.
 */
public record MailAnalyticsAcceptedMessages(
        long total,
        long submission,
        long mta) {
}
