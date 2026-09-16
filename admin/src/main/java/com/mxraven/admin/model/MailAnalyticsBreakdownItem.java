package com.mxraven.admin.model;

/**
 * One ranked item in an analytics breakdown.
 *
 * @param key stable dimension key
 * @param displayName human-readable label
 * @param resourceState current state of the dimension resource
 * @param value aggregated metric value for the item
 */
public record MailAnalyticsBreakdownItem(
        String key,
        String displayName,
        MailAnalyticsResourceState resourceState,
        long value) {
}
