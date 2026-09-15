package com.mxraven.admin.model;

import java.util.List;

/**
 * Bounded additive analytics time series for one metric and dimension.
 */
public record MailAnalyticsSeries(
        String startAt,
        String endAt,
        String queriedAt,
        String eventualConsistencyNotice,
        MailAnalyticsBucketGranularity bucketGranularity,
        MailAnalyticsMetric metric,
        MailAnalyticsDimension dimension,
        int limit,
        List<MailAnalyticsSeriesItem> series) {
}
