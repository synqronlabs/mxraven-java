package com.mxraven.admin.model;

/**
 * Size and observation horizon of the task-origin cohort.
 *
 * @param createdTasks tasks in the cohort
 * @param originEligibleTasks tasks eligible by origin
 * @param observationHours observation horizon in hours
 */
public record MailAnalyticsLifecycleCohort(
        long createdTasks,
        long originEligibleTasks,
        double observationHours) {
}
