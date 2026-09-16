package com.mxraven.admin.model;

/**
 * A labelled histogram bin with a stable key and a non-negative count.
 *
 * @param key stable bin key
 * @param label human-readable bin label
 * @param count number of samples in the bin
 */
public record MailAnalyticsCountBin(
        String key,
        String label,
        long count) {
}
