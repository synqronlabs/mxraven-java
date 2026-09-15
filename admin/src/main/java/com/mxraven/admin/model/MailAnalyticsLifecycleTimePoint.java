package com.mxraven.admin.model;

/**
 * One zero-filled lifecycle time-series bucket for the task cohort.
 */
public record MailAnalyticsLifecycleTimePoint(
        String bucketStart,
        String bucketEnd,
        long createdTasks,
        MailAnalyticsRate retryRate,
        MailAnalyticsLatencyStatistic firstAttemptLatency,
        MailAnalyticsLatencyStatistic successfulDeliveryLatency,
        MailAnalyticsLatencyStatistic terminalOutcomeLatency) {
}
