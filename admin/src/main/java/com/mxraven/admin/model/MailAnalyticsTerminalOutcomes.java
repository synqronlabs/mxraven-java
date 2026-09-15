package com.mxraven.admin.model;

/**
 * Terminal task outcomes counted by unique task ID after replacement
 * finalization.
 */
public record MailAnalyticsTerminalOutcomes(
        long succeeded,
        long failed,
        long expired,
        long suppressed) {
}
