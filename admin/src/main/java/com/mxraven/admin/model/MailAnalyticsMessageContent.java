package com.mxraven.admin.model;

import java.util.List;

/**
 * Message and rendered-content aggregates for the task cohort. Hashes and
 * message references are aggregated server-side and never returned.
 *
 * @param taskCount tasks in the cohort
 * @param logicalMessageCount distinct logical messages
 * @param tasksPerMessage tasks per logical message
 * @param renderedContentTaskCount tasks with rendered content
 * @param uniqueRenderedContentCount distinct rendered contents
 * @param tasksPerRenderedContent tasks per rendered content
 * @param tasksPerMessageDistribution distribution of tasks per message
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
