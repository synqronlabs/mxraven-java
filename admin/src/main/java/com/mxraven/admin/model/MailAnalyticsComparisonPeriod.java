package com.mxraven.admin.model;

/**
 * One interval in a comparison response.
 *
 * @param startAt inclusive UTC start of the interval
 * @param endAt exclusive UTC end of the interval
 * @param metrics summary metrics for the interval
 */
public record MailAnalyticsComparisonPeriod(
        String startAt,
        String endAt,
        MailAnalyticsMetrics metrics) {
}
