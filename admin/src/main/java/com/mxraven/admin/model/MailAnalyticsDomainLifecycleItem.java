package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Lifecycle rates and delivery latency for one first envelope recipient domain.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsDomainLifecycleItem {
    private final String recipientDomain;
    private final long createdTasks;
    private final long succeededTasks;
    private final long failedTasks;
    private final long expiredTasks;
    private final long suppressedTasks;
    private final MailAnalyticsRate deliverySuccessRate;
    private final MailAnalyticsRate retryRate;
    private final MailAnalyticsRate terminalObservedRate;
    private final MailAnalyticsLatencyStatistic successfulDeliveryLatency;

    /** first envelope recipient domain */
    public String recipientDomain() {
        return recipientDomain;
    }

    /** tasks created for the domain */
    public long createdTasks() {
        return createdTasks;
    }

    /** tasks that succeeded */
    public long succeededTasks() {
        return succeededTasks;
    }

    /** tasks that failed */
    public long failedTasks() {
        return failedTasks;
    }

    /** tasks that expired */
    public long expiredTasks() {
        return expiredTasks;
    }

    /** tasks that were suppressed */
    public long suppressedTasks() {
        return suppressedTasks;
    }

    /** successful-delivery rate */
    public MailAnalyticsRate deliverySuccessRate() {
        return deliverySuccessRate;
    }

    /** retry rate */
    public MailAnalyticsRate retryRate() {
        return retryRate;
    }

    /** rate of tasks with an observed terminal outcome */
    public MailAnalyticsRate terminalObservedRate() {
        return terminalObservedRate;
    }

    /** latency of successful deliveries */
    public MailAnalyticsLatencyStatistic successfulDeliveryLatency() {
        return successfulDeliveryLatency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsDomainLifecycleItem that = (MailAnalyticsDomainLifecycleItem) o;
        return Objects.equals(this.recipientDomain, that.recipientDomain)
                && this.createdTasks == that.createdTasks
                && this.succeededTasks == that.succeededTasks
                && this.failedTasks == that.failedTasks
                && this.expiredTasks == that.expiredTasks
                && this.suppressedTasks == that.suppressedTasks
                && Objects.equals(this.deliverySuccessRate, that.deliverySuccessRate)
                && Objects.equals(this.retryRate, that.retryRate)
                && Objects.equals(this.terminalObservedRate, that.terminalObservedRate)
                && Objects.equals(this.successfulDeliveryLatency, that.successfulDeliveryLatency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.recipientDomain, this.createdTasks, this.succeededTasks, this.failedTasks, this.expiredTasks, this.suppressedTasks, this.deliverySuccessRate, this.retryRate, this.terminalObservedRate, this.successfulDeliveryLatency);
    }

    @Override
    public String toString() {
        return "MailAnalyticsDomainLifecycleItem[" + "recipientDomain=" + this.recipientDomain + ", " + "createdTasks=" + this.createdTasks + ", " + "succeededTasks=" + this.succeededTasks + ", " + "failedTasks=" + this.failedTasks + ", " + "expiredTasks=" + this.expiredTasks + ", " + "suppressedTasks=" + this.suppressedTasks + ", " + "deliverySuccessRate=" + this.deliverySuccessRate + ", " + "retryRate=" + this.retryRate + ", " + "terminalObservedRate=" + this.terminalObservedRate + ", " + "successfulDeliveryLatency=" + this.successfulDeliveryLatency + "]";
    }

    /**
     * Creates a new MailAnalyticsDomainLifecycleItem.
     *
     * @param recipientDomain first envelope recipient domain
     * @param createdTasks tasks created for the domain
     * @param succeededTasks tasks that succeeded
     * @param failedTasks tasks that failed
     * @param expiredTasks tasks that expired
     * @param suppressedTasks tasks that were suppressed
     * @param deliverySuccessRate successful-delivery rate
     * @param retryRate retry rate
     * @param terminalObservedRate rate of tasks with an observed terminal outcome
     * @param successfulDeliveryLatency latency of successful deliveries
     */
    @JsonCreator
    public MailAnalyticsDomainLifecycleItem(String recipientDomain, long createdTasks, long succeededTasks, long failedTasks, long expiredTasks, long suppressedTasks, MailAnalyticsRate deliverySuccessRate, MailAnalyticsRate retryRate, MailAnalyticsRate terminalObservedRate, MailAnalyticsLatencyStatistic successfulDeliveryLatency) {
        this.recipientDomain = recipientDomain;
        this.createdTasks = createdTasks;
        this.succeededTasks = succeededTasks;
        this.failedTasks = failedTasks;
        this.expiredTasks = expiredTasks;
        this.suppressedTasks = suppressedTasks;
        this.deliverySuccessRate = deliverySuccessRate;
        this.retryRate = retryRate;
        this.terminalObservedRate = terminalObservedRate;
        this.successfulDeliveryLatency = successfulDeliveryLatency;
    }
}
