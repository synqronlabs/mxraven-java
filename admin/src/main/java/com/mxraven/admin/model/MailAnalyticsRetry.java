package com.mxraven.admin.model;

import java.util.List;

/**
 * Retry aggregates for the task cohort, including the final-attempt
 * distribution.
 */
public record MailAnalyticsRetry(
        long attemptedTasks,
        long multiAttemptTasks,
        MailAnalyticsRate rate,
        List<MailAnalyticsCountBin> finalAttemptDistribution) {
}
