package com.mxraven.admin.model;

/**
 * Lifecycle reach funnel counts for the task cohort.
 *
 * @param createdTasks tasks created
 * @param attemptedTasks tasks with at least one attempt
 * @param terminalTasks tasks with a terminal outcome
 */
public record MailAnalyticsLifecycleFunnel(
        long createdTasks,
        long attemptedTasks,
        long terminalTasks) {
}
