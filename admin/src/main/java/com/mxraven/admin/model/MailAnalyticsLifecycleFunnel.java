package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Lifecycle reach funnel counts for the task cohort.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsLifecycleFunnel {
    private final long createdTasks;
    private final long attemptedTasks;
    private final long terminalTasks;

    /** tasks created */
    public long createdTasks() {
        return createdTasks;
    }

    /** tasks with at least one attempt */
    public long attemptedTasks() {
        return attemptedTasks;
    }

    /** tasks with a terminal outcome */
    public long terminalTasks() {
        return terminalTasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsLifecycleFunnel that = (MailAnalyticsLifecycleFunnel) o;
        return this.createdTasks == that.createdTasks
                && this.attemptedTasks == that.attemptedTasks
                && this.terminalTasks == that.terminalTasks;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.createdTasks, this.attemptedTasks, this.terminalTasks);
    }

    @Override
    public String toString() {
        return "MailAnalyticsLifecycleFunnel[" + "createdTasks=" + this.createdTasks + ", " + "attemptedTasks=" + this.attemptedTasks + ", " + "terminalTasks=" + this.terminalTasks + "]";
    }

    /**
     * Creates a new MailAnalyticsLifecycleFunnel.
     *
     * @param createdTasks tasks created
     * @param attemptedTasks tasks with at least one attempt
     * @param terminalTasks tasks with a terminal outcome
     */
    @JsonCreator
    public MailAnalyticsLifecycleFunnel(long createdTasks, long attemptedTasks, long terminalTasks) {
        this.createdTasks = createdTasks;
        this.attemptedTasks = attemptedTasks;
        this.terminalTasks = terminalTasks;
    }
}
