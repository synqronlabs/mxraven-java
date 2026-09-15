package com.mxraven.admin.model;

/**
 * A labelled histogram bin with a stable key and a non-negative count.
 */
public record MailAnalyticsCountBin(
        String key,
        String label,
        long count) {
}
