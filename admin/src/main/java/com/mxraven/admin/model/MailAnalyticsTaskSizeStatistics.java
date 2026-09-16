package com.mxraven.admin.model;

import java.util.List;

/**
 * Zero-filled UTC buckets with averages and approximate t-digest percentiles
 * over task-created message sizes.
 *
 * @param startAt                   start of the queried window
 * @param endAt                     end of the queried window
 * @param queriedAt                 time the statistics were computed
 * @param eventualConsistencyNotice notice that recently ingested data may lag
 * @param bucketGranularity         width of each time bucket
 * @param buckets                   per-bucket size statistics
 */
public record MailAnalyticsTaskSizeStatistics(
        String startAt,
        String endAt,
        String queriedAt,
        String eventualConsistencyNotice,
        MailAnalyticsBucketGranularity bucketGranularity,
        List<MailAnalyticsTaskSizeBucket> buckets) {
}
