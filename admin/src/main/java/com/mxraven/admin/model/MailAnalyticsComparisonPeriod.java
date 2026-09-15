package com.mxraven.admin.model;

/**
 * One interval in a comparison response.
 */
public record MailAnalyticsComparisonPeriod(
        String startAt,
        String endAt,
        MailAnalyticsMetrics metrics) {
}
