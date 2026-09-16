package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * One zero-filled lifecycle time-series bucket for the task cohort.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsLifecycleTimePoint {
    private final String bucketStart;
    private final String bucketEnd;
    private final long createdTasks;
    private final MailAnalyticsRate retryRate;
    private final MailAnalyticsLatencyStatistic firstAttemptLatency;
    private final MailAnalyticsLatencyStatistic successfulDeliveryLatency;
    private final MailAnalyticsLatencyStatistic terminalOutcomeLatency;

    /** inclusive UTC start of the bucket */
    public String bucketStart() {
        return bucketStart;
    }

    /** exclusive UTC end of the bucket */
    public String bucketEnd() {
        return bucketEnd;
    }

    /** tasks created in the bucket */
    public long createdTasks() {
        return createdTasks;
    }

    /** retry rate in the bucket */
    public MailAnalyticsRate retryRate() {
        return retryRate;
    }

    /** first-attempt latency in the bucket */
    public MailAnalyticsLatencyStatistic firstAttemptLatency() {
        return firstAttemptLatency;
    }

    /** successful-delivery latency in the bucket */
    public MailAnalyticsLatencyStatistic successfulDeliveryLatency() {
        return successfulDeliveryLatency;
    }

    /** terminal-outcome latency in the bucket */
    public MailAnalyticsLatencyStatistic terminalOutcomeLatency() {
        return terminalOutcomeLatency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsLifecycleTimePoint that = (MailAnalyticsLifecycleTimePoint) o;
        return Objects.equals(this.bucketStart, that.bucketStart)
                && Objects.equals(this.bucketEnd, that.bucketEnd)
                && this.createdTasks == that.createdTasks
                && Objects.equals(this.retryRate, that.retryRate)
                && Objects.equals(this.firstAttemptLatency, that.firstAttemptLatency)
                && Objects.equals(this.successfulDeliveryLatency, that.successfulDeliveryLatency)
                && Objects.equals(this.terminalOutcomeLatency, that.terminalOutcomeLatency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.bucketStart, this.bucketEnd, this.createdTasks, this.retryRate, this.firstAttemptLatency, this.successfulDeliveryLatency, this.terminalOutcomeLatency);
    }

    @Override
    public String toString() {
        return "MailAnalyticsLifecycleTimePoint[" + "bucketStart=" + this.bucketStart + ", " + "bucketEnd=" + this.bucketEnd + ", " + "createdTasks=" + this.createdTasks + ", " + "retryRate=" + this.retryRate + ", " + "firstAttemptLatency=" + this.firstAttemptLatency + ", " + "successfulDeliveryLatency=" + this.successfulDeliveryLatency + ", " + "terminalOutcomeLatency=" + this.terminalOutcomeLatency + "]";
    }

    /**
     * Creates a new MailAnalyticsLifecycleTimePoint.
     *
     * @param bucketStart inclusive UTC start of the bucket
     * @param bucketEnd exclusive UTC end of the bucket
     * @param createdTasks tasks created in the bucket
     * @param retryRate retry rate in the bucket
     * @param firstAttemptLatency first-attempt latency in the bucket
     * @param successfulDeliveryLatency successful-delivery latency in the bucket
     * @param terminalOutcomeLatency terminal-outcome latency in the bucket
     */
    @JsonCreator
    public MailAnalyticsLifecycleTimePoint(String bucketStart, String bucketEnd, long createdTasks, MailAnalyticsRate retryRate, MailAnalyticsLatencyStatistic firstAttemptLatency, MailAnalyticsLatencyStatistic successfulDeliveryLatency, MailAnalyticsLatencyStatistic terminalOutcomeLatency) {
        this.bucketStart = bucketStart;
        this.bucketEnd = bucketEnd;
        this.createdTasks = createdTasks;
        this.retryRate = retryRate;
        this.firstAttemptLatency = firstAttemptLatency;
        this.successfulDeliveryLatency = successfulDeliveryLatency;
        this.terminalOutcomeLatency = terminalOutcomeLatency;
    }
}
