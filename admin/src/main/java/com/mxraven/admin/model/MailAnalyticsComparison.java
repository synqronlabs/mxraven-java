package com.mxraven.admin.model;

/**
 * Overview metrics for the requested interval and the immediately preceding
 * interval of equal duration.
 */
public record MailAnalyticsComparison(
        String queriedAt,
        String eventualConsistencyNotice,
        MailAnalyticsComparisonPeriod current,
        MailAnalyticsComparisonPeriod preceding) {
}
