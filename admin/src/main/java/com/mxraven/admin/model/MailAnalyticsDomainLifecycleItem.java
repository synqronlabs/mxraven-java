package com.mxraven.admin.model;

/**
 * Lifecycle rates and delivery latency for one first envelope recipient domain.
 *
 * @param recipientDomain first envelope recipient domain
 * @param createdTasks tasks created for the domain
 * @param succeededTasks tasks that succeeded
 * @param failedTasks tasks that failed
 * @param expiredTasks tasks that expired
 * @param suppressedTasks tasks that were suppressed
 * @param deliverySuccessRate successful-delivery rate
 * @param retryRate retry rate
 * @param terminalObservedRate rate of tasks with an observed terminal outcome
 * @param successfulDeliveryLatency latency of successful deliveries
 */
public record MailAnalyticsDomainLifecycleItem(
        String recipientDomain,
        long createdTasks,
        long succeededTasks,
        long failedTasks,
        long expiredTasks,
        long suppressedTasks,
        MailAnalyticsRate deliverySuccessRate,
        MailAnalyticsRate retryRate,
        MailAnalyticsRate terminalObservedRate,
        MailAnalyticsLatencyStatistic successfulDeliveryLatency) {
}
