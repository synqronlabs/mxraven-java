package com.mxraven.admin.model;

/**
 * Lifecycle reach funnel counts for the task cohort.
 */
public record MailAnalyticsLifecycleFunnel(
        long createdTasks,
        long attemptedTasks,
        long terminalTasks) {
}
