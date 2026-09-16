package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * Retry aggregates for the task cohort, including the final-attempt
 * distribution.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsRetry {
    private final long attemptedTasks;
    private final long multiAttemptTasks;
    private final MailAnalyticsRate rate;
    private final List<MailAnalyticsCountBin> finalAttemptDistribution;

    /** tasks with at least one attempt */
    public long attemptedTasks() {
        return attemptedTasks;
    }

    /** tasks with more than one attempt */
    public long multiAttemptTasks() {
        return multiAttemptTasks;
    }

    /** retry rate */
    public MailAnalyticsRate rate() {
        return rate;
    }

    /** distribution of attempts on the final attempt */
    public List<MailAnalyticsCountBin> finalAttemptDistribution() {
        return finalAttemptDistribution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsRetry that = (MailAnalyticsRetry) o;
        return this.attemptedTasks == that.attemptedTasks
                && this.multiAttemptTasks == that.multiAttemptTasks
                && Objects.equals(this.rate, that.rate)
                && Objects.equals(this.finalAttemptDistribution, that.finalAttemptDistribution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.attemptedTasks, this.multiAttemptTasks, this.rate, this.finalAttemptDistribution);
    }

    @Override
    public String toString() {
        return "MailAnalyticsRetry[" + "attemptedTasks=" + this.attemptedTasks + ", " + "multiAttemptTasks=" + this.multiAttemptTasks + ", " + "rate=" + this.rate + ", " + "finalAttemptDistribution=" + this.finalAttemptDistribution + "]";
    }

    /**
     * Creates a new MailAnalyticsRetry.
     *
     * @param attemptedTasks tasks with at least one attempt
     * @param multiAttemptTasks tasks with more than one attempt
     * @param rate retry rate
     * @param finalAttemptDistribution distribution of attempts on the final attempt
     */
    @JsonCreator
    public MailAnalyticsRetry(long attemptedTasks, long multiAttemptTasks, MailAnalyticsRate rate, List<MailAnalyticsCountBin> finalAttemptDistribution) {
        this.attemptedTasks = attemptedTasks;
        this.multiAttemptTasks = multiAttemptTasks;
        this.rate = rate;
        this.finalAttemptDistribution = finalAttemptDistribution;
    }
}
