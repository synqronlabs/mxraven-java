package com.mxraven.admin.model;

import java.util.List;

/**
 * Task-cohort lifecycle analytics: reach, retries, observed latency, and
 * message/content aggregates.
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
