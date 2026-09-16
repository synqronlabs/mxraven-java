package com.mxraven.admin.model;

import java.util.List;

/**
 * Retry aggregates for the task cohort, including the final-attempt
 * distribution.
 *
 * @param attemptedTasks tasks with at least one attempt
 * @param multiAttemptTasks tasks with more than one attempt
 * @param rate retry rate
 * @param finalAttemptDistribution distribution of attempts on the final attempt
 */
public record MailAnalyticsRetry(
        long attemptedTasks,
        long multiAttemptTasks,
        MailAnalyticsRate rate,
        List<MailAnalyticsCountBin> finalAttemptDistribution) {
}
