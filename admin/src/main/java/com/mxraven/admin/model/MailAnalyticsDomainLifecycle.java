package com.mxraven.admin.model;

import java.util.List;

/**
 * Bounded recipient-domain lifecycle analytics over a task-origin cohort.
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
