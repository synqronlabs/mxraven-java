package com.mxraven.admin.model;

import java.util.List;

/**
 * UTC task activity heatmap with exactly 168 zero-filled weekday/hour cells.
 */
public record MailAnalyticsActivityHeatmap(
        String startAt,
        String endAt,
        String queriedAt,
        String eventualConsistencyNotice,
        MailAnalyticsTimezone timezone,
        MailAnalyticsMetric metric,
        List<MailAnalyticsActivityHeatmapCell> cells) {
}
