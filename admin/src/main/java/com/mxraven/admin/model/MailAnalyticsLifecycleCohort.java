package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Size and observation horizon of the task-origin cohort.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsLifecycleCohort {
    private final long createdTasks;
    private final long originEligibleTasks;
    private final double observationHours;

    /** tasks in the cohort */
    public long createdTasks() {
        return createdTasks;
    }

    /** tasks eligible by origin */
    public long originEligibleTasks() {
        return originEligibleTasks;
    }

    /** observation horizon in hours */
    public double observationHours() {
        return observationHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsLifecycleCohort that = (MailAnalyticsLifecycleCohort) o;
        return this.createdTasks == that.createdTasks
                && this.originEligibleTasks == that.originEligibleTasks
                && Double.compare(this.observationHours, that.observationHours) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.createdTasks, this.originEligibleTasks, this.observationHours);
    }

    @Override
    public String toString() {
        return "MailAnalyticsLifecycleCohort[" + "createdTasks=" + this.createdTasks + ", " + "originEligibleTasks=" + this.originEligibleTasks + ", " + "observationHours=" + this.observationHours + "]";
    }

    /**
     * Creates a new MailAnalyticsLifecycleCohort.
     *
     * @param createdTasks tasks in the cohort
     * @param originEligibleTasks tasks eligible by origin
     * @param observationHours observation horizon in hours
     */
    @JsonCreator
    public MailAnalyticsLifecycleCohort(long createdTasks, long originEligibleTasks, double observationHours) {
        this.createdTasks = createdTasks;
        this.originEligibleTasks = originEligibleTasks;
        this.observationHours = observationHours;
    }
}
