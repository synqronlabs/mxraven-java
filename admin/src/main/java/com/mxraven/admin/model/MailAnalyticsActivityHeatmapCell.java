package com.mxraven.admin.model;

/**
 * One weekday/hour heatmap cell. Weekdays use ISO numbering where Monday is 1
 * and Sunday is 7.
 *
 * @param isoWeekday ISO weekday number, Monday {@code 1} through Sunday {@code 7}
 * @param hour UTC hour of day, {@code 0} through {@code 23}
 * @param egressTasks egress tasks observed in this weekday/hour cell
 */
public record MailAnalyticsActivityHeatmapCell(
        int isoWeekday,
        int hour,
        long egressTasks) {
}
