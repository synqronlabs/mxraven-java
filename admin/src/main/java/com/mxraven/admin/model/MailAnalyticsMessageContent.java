package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * Message and rendered-content aggregates for the task cohort. Hashes and
 * message references are aggregated server-side and never returned.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsMessageContent {
    private final long taskCount;
    private final long logicalMessageCount;
    private final MailAnalyticsRatio tasksPerMessage;
    private final long renderedContentTaskCount;
    private final long uniqueRenderedContentCount;
    private final MailAnalyticsRatio tasksPerRenderedContent;
    private final List<MailAnalyticsCountBin> tasksPerMessageDistribution;

    /** tasks in the cohort */
    public long taskCount() {
        return taskCount;
    }

    /** distinct logical messages */
    public long logicalMessageCount() {
        return logicalMessageCount;
    }

    /** tasks per logical message */
    public MailAnalyticsRatio tasksPerMessage() {
        return tasksPerMessage;
    }

    /** tasks with rendered content */
    public long renderedContentTaskCount() {
        return renderedContentTaskCount;
    }

    /** distinct rendered contents */
    public long uniqueRenderedContentCount() {
        return uniqueRenderedContentCount;
    }

    /** tasks per rendered content */
    public MailAnalyticsRatio tasksPerRenderedContent() {
        return tasksPerRenderedContent;
    }

    /** distribution of tasks per message */
    public List<MailAnalyticsCountBin> tasksPerMessageDistribution() {
        return tasksPerMessageDistribution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsMessageContent that = (MailAnalyticsMessageContent) o;
        return this.taskCount == that.taskCount
                && this.logicalMessageCount == that.logicalMessageCount
                && Objects.equals(this.tasksPerMessage, that.tasksPerMessage)
                && this.renderedContentTaskCount == that.renderedContentTaskCount
                && this.uniqueRenderedContentCount == that.uniqueRenderedContentCount
                && Objects.equals(this.tasksPerRenderedContent, that.tasksPerRenderedContent)
                && Objects.equals(this.tasksPerMessageDistribution, that.tasksPerMessageDistribution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.taskCount, this.logicalMessageCount, this.tasksPerMessage, this.renderedContentTaskCount, this.uniqueRenderedContentCount, this.tasksPerRenderedContent, this.tasksPerMessageDistribution);
    }

    @Override
    public String toString() {
        return "MailAnalyticsMessageContent[" + "taskCount=" + this.taskCount + ", " + "logicalMessageCount=" + this.logicalMessageCount + ", " + "tasksPerMessage=" + this.tasksPerMessage + ", " + "renderedContentTaskCount=" + this.renderedContentTaskCount + ", " + "uniqueRenderedContentCount=" + this.uniqueRenderedContentCount + ", " + "tasksPerRenderedContent=" + this.tasksPerRenderedContent + ", " + "tasksPerMessageDistribution=" + this.tasksPerMessageDistribution + "]";
    }

    /**
     * Creates a new MailAnalyticsMessageContent.
     *
     * @param taskCount tasks in the cohort
     * @param logicalMessageCount distinct logical messages
     * @param tasksPerMessage tasks per logical message
     * @param renderedContentTaskCount tasks with rendered content
     * @param uniqueRenderedContentCount distinct rendered contents
     * @param tasksPerRenderedContent tasks per rendered content
     * @param tasksPerMessageDistribution distribution of tasks per message
     */
    @JsonCreator
    public MailAnalyticsMessageContent(long taskCount, long logicalMessageCount, MailAnalyticsRatio tasksPerMessage, long renderedContentTaskCount, long uniqueRenderedContentCount, MailAnalyticsRatio tasksPerRenderedContent, List<MailAnalyticsCountBin> tasksPerMessageDistribution) {
        this.taskCount = taskCount;
        this.logicalMessageCount = logicalMessageCount;
        this.tasksPerMessage = tasksPerMessage;
        this.renderedContentTaskCount = renderedContentTaskCount;
        this.uniqueRenderedContentCount = uniqueRenderedContentCount;
        this.tasksPerRenderedContent = tasksPerRenderedContent;
        this.tasksPerMessageDistribution = tasksPerMessageDistribution;
    }
}
