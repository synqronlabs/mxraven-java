package com.mxraven.admin.model;

import java.util.List;

/**
 * One dimension value and its zero-filled points within a series.
 */
public record MailAnalyticsSeriesItem(
        String key,
        String displayName,
        MailAnalyticsResourceState resourceState,
        List<MailAnalyticsSeriesPoint> points) {
}
