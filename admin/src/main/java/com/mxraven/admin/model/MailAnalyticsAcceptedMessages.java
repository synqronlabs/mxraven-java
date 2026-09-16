package com.mxraven.admin.model;

/**
 * Accepted message counts split by submission and MTA channel.
 *
 * @param total total accepted messages
 * @param submission messages accepted through submission listeners
 * @param mta messages accepted through MTA listeners
 */
public record MailAnalyticsAcceptedMessages(
        long total,
        long submission,
        long mta) {
}
