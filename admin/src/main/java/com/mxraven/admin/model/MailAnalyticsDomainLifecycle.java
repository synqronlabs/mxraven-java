package com.mxraven.admin.model;

import java.util.List;

/**
 * Bounded recipient-domain lifecycle analytics over a task-origin cohort.
 *
 * @param startAt inclusive UTC start of the task-origin cohort
 * @param endAt exclusive UTC end of the task-origin cohort
 * @param observationEndAt exclusive UTC end of the observation horizon
 * @param queriedAt UTC timestamp when the query was evaluated
 * @param eventualConsistencyNotice note about eventual consistency of the data
 * @param limit maximum ranked domains requested
 * @param totalCreatedTasks total tasks created across all domains
 * @param otherCreatedTasks created tasks not covered by the returned items
 * @param items per-domain lifecycle items
 * @param failureStatuses aggregated failure statuses
 */
public record MailAnalyticsDomainLifecycle(
        String startAt,
        String endAt,
        String observationEndAt,
        String queriedAt,
        String eventualConsistencyNotice,
        int limit,
        long totalCreatedTasks,
        long otherCreatedTasks,
        List<MailAnalyticsDomainLifecycleItem> items,
        List<MailAnalyticsDomainFailureStatus> failureStatuses) {
}
