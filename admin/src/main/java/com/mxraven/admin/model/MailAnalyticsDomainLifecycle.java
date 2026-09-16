package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * Bounded recipient-domain lifecycle analytics over a task-origin cohort.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsDomainLifecycle {
    private final String startAt;
    private final String endAt;
    private final String observationEndAt;
    private final String queriedAt;
    private final String eventualConsistencyNotice;
    private final int limit;
    private final long totalCreatedTasks;
    private final long otherCreatedTasks;
    private final List<MailAnalyticsDomainLifecycleItem> items;
    private final List<MailAnalyticsDomainFailureStatus> failureStatuses;

    /** inclusive UTC start of the task-origin cohort */
    public String startAt() {
        return startAt;
    }

    /** exclusive UTC end of the task-origin cohort */
    public String endAt() {
        return endAt;
    }

    /** exclusive UTC end of the observation horizon */
    public String observationEndAt() {
        return observationEndAt;
    }

    /** UTC timestamp when the query was evaluated */
    public String queriedAt() {
        return queriedAt;
    }

    /** note about eventual consistency of the data */
    public String eventualConsistencyNotice() {
        return eventualConsistencyNotice;
    }

    /** maximum ranked domains requested */
    public int limit() {
        return limit;
    }

    /** total tasks created across all domains */
    public long totalCreatedTasks() {
        return totalCreatedTasks;
    }

    /** created tasks not covered by the returned items */
    public long otherCreatedTasks() {
        return otherCreatedTasks;
    }

    /** per-domain lifecycle items */
    public List<MailAnalyticsDomainLifecycleItem> items() {
        return items;
    }

    /** aggregated failure statuses */
    public List<MailAnalyticsDomainFailureStatus> failureStatuses() {
        return failureStatuses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsDomainLifecycle that = (MailAnalyticsDomainLifecycle) o;
        return Objects.equals(this.startAt, that.startAt)
                && Objects.equals(this.endAt, that.endAt)
                && Objects.equals(this.observationEndAt, that.observationEndAt)
                && Objects.equals(this.queriedAt, that.queriedAt)
                && Objects.equals(this.eventualConsistencyNotice, that.eventualConsistencyNotice)
                && this.limit == that.limit
                && this.totalCreatedTasks == that.totalCreatedTasks
                && this.otherCreatedTasks == that.otherCreatedTasks
                && Objects.equals(this.items, that.items)
                && Objects.equals(this.failureStatuses, that.failureStatuses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.startAt, this.endAt, this.observationEndAt, this.queriedAt, this.eventualConsistencyNotice, this.limit, this.totalCreatedTasks, this.otherCreatedTasks, this.items, this.failureStatuses);
    }

    @Override
    public String toString() {
        return "MailAnalyticsDomainLifecycle[" + "startAt=" + this.startAt + ", " + "endAt=" + this.endAt + ", " + "observationEndAt=" + this.observationEndAt + ", " + "queriedAt=" + this.queriedAt + ", " + "eventualConsistencyNotice=" + this.eventualConsistencyNotice + ", " + "limit=" + this.limit + ", " + "totalCreatedTasks=" + this.totalCreatedTasks + ", " + "otherCreatedTasks=" + this.otherCreatedTasks + ", " + "items=" + this.items + ", " + "failureStatuses=" + this.failureStatuses + "]";
    }

    /**
     * Creates a new MailAnalyticsDomainLifecycle.
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
    @JsonCreator
    public MailAnalyticsDomainLifecycle(String startAt, String endAt, String observationEndAt, String queriedAt, String eventualConsistencyNotice, int limit, long totalCreatedTasks, long otherCreatedTasks, List<MailAnalyticsDomainLifecycleItem> items, List<MailAnalyticsDomainFailureStatus> failureStatuses) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.observationEndAt = observationEndAt;
        this.queriedAt = queriedAt;
        this.eventualConsistencyNotice = eventualConsistencyNotice;
        this.limit = limit;
        this.totalCreatedTasks = totalCreatedTasks;
        this.otherCreatedTasks = otherCreatedTasks;
        this.items = items;
        this.failureStatuses = failureStatuses;
    }
}
