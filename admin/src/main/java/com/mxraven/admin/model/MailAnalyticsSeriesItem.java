package com.mxraven.admin.model;

import java.util.List;

/**
 * One dimension value and its zero-filled points within a series.
 *
 * @param key           dimension value identifying this series item
 * @param displayName   human-readable label for the dimension value
 * @param resourceState state of the referenced resource, when applicable
 * @param points        zero-filled points for this dimension value
 */
public record MailAnalyticsSeriesItem(
        String key,
        String displayName,
        MailAnalyticsResourceState resourceState,
        List<MailAnalyticsSeriesPoint> points) {
}
