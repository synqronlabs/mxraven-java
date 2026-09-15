package com.mxraven.admin.model;

/**
 * Size and observation horizon of the task-origin cohort.
 */
public record MailAnalyticsLifecycleCohort(
        long createdTasks,
        long originEligibleTasks,
        double observationHours) {
}
