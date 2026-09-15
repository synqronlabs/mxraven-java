package com.mxraven.admin.model;

import java.util.List;

/**
 * Zero-filled UTC buckets with averages and approximate t-digest percentiles
 * over task-created message sizes.
 */
public record MailAnalyticsTaskSizeStatistics(
        String startAt,
        String endAt,
        String queriedAt,
        String eventualConsistencyNotice,
        MailAnalyticsBucketGranularity bucketGranularity,
        List<MailAnalyticsTaskSizeBucket> buckets) {
}
