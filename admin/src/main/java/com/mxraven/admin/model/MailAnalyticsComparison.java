package com.mxraven.admin.model;

/**
 * Overview metrics for the requested interval and the immediately preceding
 * interval of equal duration.
 *
 * @param queriedAt UTC timestamp when the query was evaluated
 * @param eventualConsistencyNotice note about eventual consistency of the data
 * @param current metrics for the requested interval
 * @param preceding metrics for the immediately preceding interval
 */
public record MailAnalyticsComparison(
        String queriedAt,
        String eventualConsistencyNotice,
        MailAnalyticsComparisonPeriod current,
        MailAnalyticsComparisonPeriod preceding) {
}
