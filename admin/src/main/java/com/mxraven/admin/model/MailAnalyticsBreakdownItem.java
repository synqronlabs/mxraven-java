package com.mxraven.admin.model;

/**
 * One ranked item in an analytics breakdown.
 */
public record MailAnalyticsBreakdownItem(
        String key,
        String displayName,
        MailAnalyticsResourceState resourceState,
        long value) {
}
