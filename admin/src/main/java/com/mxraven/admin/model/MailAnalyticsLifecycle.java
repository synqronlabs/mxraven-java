package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * Task-cohort lifecycle analytics: reach, retries, observed latency, and
 * message/content aggregates.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsLifecycle {
    private final String startAt;
    private final String endAt;
    private final String observationEndAt;
    private final MailAnalyticsBucketGranularity bucketGranularity;
    private final String queriedAt;
    private final String eventualConsistencyNotice;
    private final MailAnalyticsLifecycleCohort cohort;
    private final MailAnalyticsLifecycleFunnel funnel;
    private final MailAnalyticsRetry retry;
    private final MailAnalyticsLatencySet latency;
    private final List<MailAnalyticsLifecycleTimePoint> timeSeries;
    private final List<MailAnalyticsLatencyBySize> latencyBySize;
    private final MailAnalyticsMessageContent messageContent;

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

    /** granularity of the time-series buckets */
    public MailAnalyticsBucketGranularity bucketGranularity() {
        return bucketGranularity;
    }

    /** UTC timestamp when the query was evaluated */
    public String queriedAt() {
        return queriedAt;
    }

    /** note about eventual consistency of the data */
    public String eventualConsistencyNotice() {
        return eventualConsistencyNotice;
    }

    /** task-origin cohort shape */
    public MailAnalyticsLifecycleCohort cohort() {
        return cohort;
    }

    /** lifecycle reach funnel */
    public MailAnalyticsLifecycleFunnel funnel() {
        return funnel;
    }

    /** retry aggregates for the cohort */
    public MailAnalyticsRetry retry() {
        return retry;
    }

    /** observed latency metrics */
    public MailAnalyticsLatencySet latency() {
        return latency;
    }

    /** zero-filled lifecycle time-series buckets */
    public List<MailAnalyticsLifecycleTimePoint> timeSeries() {
        return timeSeries;
    }

    /** latency grouped by message size */
    public List<MailAnalyticsLatencyBySize> latencyBySize() {
        return latencyBySize;
    }

    /** message and rendered-content aggregates */
    public MailAnalyticsMessageContent messageContent() {
        return messageContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsLifecycle that = (MailAnalyticsLifecycle) o;
        return Objects.equals(this.startAt, that.startAt)
                && Objects.equals(this.endAt, that.endAt)
                && Objects.equals(this.observationEndAt, that.observationEndAt)
                && Objects.equals(this.bucketGranularity, that.bucketGranularity)
                && Objects.equals(this.queriedAt, that.queriedAt)
                && Objects.equals(this.eventualConsistencyNotice, that.eventualConsistencyNotice)
                && Objects.equals(this.cohort, that.cohort)
                && Objects.equals(this.funnel, that.funnel)
                && Objects.equals(this.retry, that.retry)
                && Objects.equals(this.latency, that.latency)
                && Objects.equals(this.timeSeries, that.timeSeries)
                && Objects.equals(this.latencyBySize, that.latencyBySize)
                && Objects.equals(this.messageContent, that.messageContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.startAt, this.endAt, this.observationEndAt, this.bucketGranularity, this.queriedAt, this.eventualConsistencyNotice, this.cohort, this.funnel, this.retry, this.latency, this.timeSeries, this.latencyBySize, this.messageContent);
    }

    @Override
    public String toString() {
        return "MailAnalyticsLifecycle[" + "startAt=" + this.startAt + ", " + "endAt=" + this.endAt + ", " + "observationEndAt=" + this.observationEndAt + ", " + "bucketGranularity=" + this.bucketGranularity + ", " + "queriedAt=" + this.queriedAt + ", " + "eventualConsistencyNotice=" + this.eventualConsistencyNotice + ", " + "cohort=" + this.cohort + ", " + "funnel=" + this.funnel + ", " + "retry=" + this.retry + ", " + "latency=" + this.latency + ", " + "timeSeries=" + this.timeSeries + ", " + "latencyBySize=" + this.latencyBySize + ", " + "messageContent=" + this.messageContent + "]";
    }

    /**
     * Creates a new MailAnalyticsLifecycle.
     *
     * @param startAt inclusive UTC start of the task-origin cohort
     * @param endAt exclusive UTC end of the task-origin cohort
     * @param observationEndAt exclusive UTC end of the observation horizon
     * @param bucketGranularity granularity of the time-series buckets
     * @param queriedAt UTC timestamp when the query was evaluated
     * @param eventualConsistencyNotice note about eventual consistency of the data
     * @param cohort task-origin cohort shape
     * @param funnel lifecycle reach funnel
     * @param retry retry aggregates for the cohort
     * @param latency observed latency metrics
     * @param timeSeries zero-filled lifecycle time-series buckets
     * @param latencyBySize latency grouped by message size
     * @param messageContent message and rendered-content aggregates
     */
    @JsonCreator
    public MailAnalyticsLifecycle(String startAt, String endAt, String observationEndAt, MailAnalyticsBucketGranularity bucketGranularity, String queriedAt, String eventualConsistencyNotice, MailAnalyticsLifecycleCohort cohort, MailAnalyticsLifecycleFunnel funnel, MailAnalyticsRetry retry, MailAnalyticsLatencySet latency, List<MailAnalyticsLifecycleTimePoint> timeSeries, List<MailAnalyticsLatencyBySize> latencyBySize, MailAnalyticsMessageContent messageContent) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.observationEndAt = observationEndAt;
        this.bucketGranularity = bucketGranularity;
        this.queriedAt = queriedAt;
        this.eventualConsistencyNotice = eventualConsistencyNotice;
        this.cohort = cohort;
        this.funnel = funnel;
        this.retry = retry;
        this.latency = latency;
        this.timeSeries = timeSeries;
        this.latencyBySize = latencyBySize;
        this.messageContent = messageContent;
    }
}
