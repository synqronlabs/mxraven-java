package com.mxraven.admin.model;

/**
 * Lifecycle rates and delivery latency for one first envelope recipient domain.
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
