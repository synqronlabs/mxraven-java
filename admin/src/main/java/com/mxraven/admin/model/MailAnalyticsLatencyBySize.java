package com.mxraven.admin.model;

/**
 * Terminal-outcome latency for one message-size bin.
 */
public record MailAnalyticsLatencyBySize(
        MailAnalyticsSizeBin sizeBin,
        long sampleCount,
        MailAnalyticsLatencyStatistic terminalOutcomeLatency) {
}
