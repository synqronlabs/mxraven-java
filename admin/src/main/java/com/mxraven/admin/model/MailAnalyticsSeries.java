package com.mxraven.admin.model;

import java.util.List;

/**
 * Bounded additive analytics time series for one metric and dimension.
 *
 * @param startAt                   start of the queried window
 * @param endAt                     end of the queried window
 * @param queriedAt                 time the series was computed
 * @param eventualConsistencyNotice notice that recently ingested data may lag
 * @param bucketGranularity         width of each time bucket
 * @param metric                    metric aggregated by the series
 * @param dimension                 dimension used to split the metric
 * @param limit                     maximum number of series items returned
 * @param series                    per-dimension series items
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
