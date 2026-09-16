package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * Tenant mail analytics overview: summary metrics, a zero-filled UTC time
 * series, and a bounded listener breakdown for the requested interval.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsOverview {
    private final String startAt;
    private final String endAt;
    private final MailAnalyticsBucketGranularity bucketGranularity;
    private final String queriedAt;
    private final String eventualConsistencyNotice;
    private final MailAnalyticsMetrics summary;
    private final List<MailAnalyticsTimePoint> timeSeries;
    private final MailAnalyticsListenerBreakdown listenerBreakdown;

    /** inclusive UTC start of the interval */
    public String startAt() {
        return startAt;
    }

    /** exclusive UTC end of the interval */
    public String endAt() {
        return endAt;
    }

    /** granularity of the time-series buckets */
    public MailAnalyticsBucketGranularity bucketGranularity() {
        return bucketGranularity;
    }

    /** UTC timestamp when the query was evaluated */
    public String queriedAt() {
        return queriedAt;
    }

    /** note about eventual consistency of the data */
    public String eventualConsistencyNotice() {
        return eventualConsistencyNotice;
    }

    /** summary metrics for the interval */
    public MailAnalyticsMetrics summary() {
        return summary;
    }

    /** zero-filled UTC time-series points */
    public List<MailAnalyticsTimePoint> timeSeries() {
        return timeSeries;
    }

    /** bounded listener breakdown */
    public MailAnalyticsListenerBreakdown listenerBreakdown() {
        return listenerBreakdown;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsOverview that = (MailAnalyticsOverview) o;
        return Objects.equals(this.startAt, that.startAt)
                && Objects.equals(this.endAt, that.endAt)
                && Objects.equals(this.bucketGranularity, that.bucketGranularity)
                && Objects.equals(this.queriedAt, that.queriedAt)
                && Objects.equals(this.eventualConsistencyNotice, that.eventualConsistencyNotice)
                && Objects.equals(this.summary, that.summary)
                && Objects.equals(this.timeSeries, that.timeSeries)
                && Objects.equals(this.listenerBreakdown, that.listenerBreakdown);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.startAt, this.endAt, this.bucketGranularity, this.queriedAt, this.eventualConsistencyNotice, this.summary, this.timeSeries, this.listenerBreakdown);
    }

    @Override
    public String toString() {
        return "MailAnalyticsOverview[" + "startAt=" + this.startAt + ", " + "endAt=" + this.endAt + ", " + "bucketGranularity=" + this.bucketGranularity + ", " + "queriedAt=" + this.queriedAt + ", " + "eventualConsistencyNotice=" + this.eventualConsistencyNotice + ", " + "summary=" + this.summary + ", " + "timeSeries=" + this.timeSeries + ", " + "listenerBreakdown=" + this.listenerBreakdown + "]";
    }

    /**
     * Creates a new MailAnalyticsOverview.
     *
     * @param startAt inclusive UTC start of the interval
     * @param endAt exclusive UTC end of the interval
     * @param bucketGranularity granularity of the time-series buckets
     * @param queriedAt UTC timestamp when the query was evaluated
     * @param eventualConsistencyNotice note about eventual consistency of the data
     * @param summary summary metrics for the interval
     * @param timeSeries zero-filled UTC time-series points
     * @param listenerBreakdown bounded listener breakdown
     */
    @JsonCreator
    public MailAnalyticsOverview(String startAt, String endAt, MailAnalyticsBucketGranularity bucketGranularity, String queriedAt, String eventualConsistencyNotice, MailAnalyticsMetrics summary, List<MailAnalyticsTimePoint> timeSeries, MailAnalyticsListenerBreakdown listenerBreakdown) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.bucketGranularity = bucketGranularity;
        this.queriedAt = queriedAt;
        this.eventualConsistencyNotice = eventualConsistencyNotice;
        this.summary = summary;
        this.timeSeries = timeSeries;
        this.listenerBreakdown = listenerBreakdown;
    }
}
