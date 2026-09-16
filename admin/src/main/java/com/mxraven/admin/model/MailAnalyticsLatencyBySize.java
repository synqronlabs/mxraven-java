package com.mxraven.admin.model;

/**
 * Terminal-outcome latency for one message-size bin.
 *
 * @param sizeBin message-size bin
 * @param sampleCount number of samples in the bin
 * @param terminalOutcomeLatency terminal-outcome latency for the bin
 */
public record MailAnalyticsLatencyBySize(
        MailAnalyticsSizeBin sizeBin,
        long sampleCount,
        MailAnalyticsLatencyStatistic terminalOutcomeLatency) {
}
