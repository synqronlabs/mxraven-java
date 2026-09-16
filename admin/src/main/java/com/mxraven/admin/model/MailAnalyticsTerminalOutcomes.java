package com.mxraven.admin.model;

/**
 * Terminal task outcomes counted by unique task ID after replacement
 * finalization.
 *
 * @param succeeded  tasks that succeeded
 * @param failed     tasks that failed
 * @param expired    tasks that expired
 * @param suppressed tasks that were suppressed
 */
public record MailAnalyticsTerminalOutcomes(
        long succeeded,
        long failed,
        long expired,
        long suppressed) {
}
