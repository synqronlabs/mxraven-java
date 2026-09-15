package com.mxraven.admin.model;

/**
 * One weekday/hour heatmap cell. Weekdays use ISO numbering where Monday is 1
 * and Sunday is 7.
 */
public record MailAnalyticsActivityHeatmapCell(
        int isoWeekday,
        int hour,
        long egressTasks) {
}
