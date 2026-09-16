package com.mxraven.admin.model;

/**
 * One zero-filled lifecycle time-series bucket for the task cohort.
 *
 * @param bucketStart inclusive UTC start of the bucket
 * @param bucketEnd exclusive UTC end of the bucket
 * @param createdTasks tasks created in the bucket
 * @param retryRate retry rate in the bucket
 * @param firstAttemptLatency first-attempt latency in the bucket
 * @param successfulDeliveryLatency successful-delivery latency in the bucket
 * @param terminalOutcomeLatency terminal-outcome latency in the bucket
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
