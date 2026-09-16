package com.mxraven.admin.model;

import java.util.List;

/**
 * UTC task activity heatmap with exactly 168 zero-filled weekday/hour cells.
 *
 * @param startAt inclusive UTC start of the interval
 * @param endAt exclusive UTC end of the interval
 * @param queriedAt UTC timestamp when the query was evaluated
 * @param eventualConsistencyNotice note about eventual consistency of the data
 * @param timezone timezone used to bucket the heatmap
 * @param metric metric plotted in the heatmap
 * @param cells the zero-filled weekday/hour cells
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
