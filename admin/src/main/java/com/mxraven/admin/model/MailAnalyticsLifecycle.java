package com.mxraven.admin.model;

import java.util.List;

/**
 * Task-cohort lifecycle analytics: reach, retries, observed latency, and
 * message/content aggregates.
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
public record MailAnalyticsLifecycle(
        String startAt,
        String endAt,
        String observationEndAt,
        MailAnalyticsBucketGranularity bucketGranularity,
        String queriedAt,
        String eventualConsistencyNotice,
        MailAnalyticsLifecycleCohort cohort,
        MailAnalyticsLifecycleFunnel funnel,
        MailAnalyticsRetry retry,
        MailAnalyticsLatencySet latency,
        List<MailAnalyticsLifecycleTimePoint> timeSeries,
        List<MailAnalyticsLatencyBySize> latencyBySize,
        MailAnalyticsMessageContent messageContent) {
}
