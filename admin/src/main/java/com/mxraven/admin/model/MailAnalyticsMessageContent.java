package com.mxraven.admin.model;

import java.util.List;

/**
 * Message and rendered-content aggregates for the task cohort. Hashes and
 * message references are aggregated server-side and never returned.
 */
public record MailAnalyticsMessageContent(
        long taskCount,
        long logicalMessageCount,
        MailAnalyticsRatio tasksPerMessage,
        long renderedContentTaskCount,
        long uniqueRenderedContentCount,
        MailAnalyticsRatio tasksPerRenderedContent,
        List<MailAnalyticsCountBin> tasksPerMessageDistribution) {
}
