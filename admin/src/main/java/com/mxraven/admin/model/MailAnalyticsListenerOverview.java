package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * Fixed per-listener mail-analytics catalog with a zero-filled UTC time series.
 *
 * <p>The interval is start-inclusive and end-exclusive and may span at most 31
 * days; the server selects hourly buckets for intervals up to 72 hours and
 * daily buckets for longer intervals. Time-series buckets exactly partition the
 * interval and summary values equal the corresponding time-series totals.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsListenerOverview {
    private final String startAt;
    private final String endAt;
    private final MailAnalyticsBucketGranularity bucketGranularity;
    private final String queriedAt;
    private final String eventualConsistencyNotice;
    private final MailAnalyticsListenerIdentity listener;
    private final MailAnalyticsListenerOverviewMetrics summary;
    private final List<MailAnalyticsListenerOverviewTimePoint> timeSeries;

    /** inclusive UTC start of the analytics interval */
    public String startAt() {
        return startAt;
    }

    /** exclusive UTC end of the analytics interval */
    public String endAt() {
        return endAt;
    }

    /** bucket granularity selected by the server */
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

    /** identity of the reported listener */
    public MailAnalyticsListenerIdentity listener() {
        return listener;
    }

    /** summary metrics for the interval */
    public MailAnalyticsListenerOverviewMetrics summary() {
        return summary;
    }

    /** zero-filled buckets ordered oldest first */
    public List<MailAnalyticsListenerOverviewTimePoint> timeSeries() {
        return timeSeries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsListenerOverview that = (MailAnalyticsListenerOverview) o;
        return Objects.equals(this.startAt, that.startAt)
                && Objects.equals(this.endAt, that.endAt)
                && Objects.equals(this.bucketGranularity, that.bucketGranularity)
                && Objects.equals(this.queriedAt, that.queriedAt)
                && Objects.equals(this.eventualConsistencyNotice, that.eventualConsistencyNotice)
                && Objects.equals(this.listener, that.listener)
                && Objects.equals(this.summary, that.summary)
                && Objects.equals(this.timeSeries, that.timeSeries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.startAt, this.endAt, this.bucketGranularity, this.queriedAt, this.eventualConsistencyNotice, this.listener, this.summary, this.timeSeries);
    }

    @Override
    public String toString() {
        return "MailAnalyticsListenerOverview[" + "startAt=" + this.startAt + ", " + "endAt=" + this.endAt + ", " + "bucketGranularity=" + this.bucketGranularity + ", " + "queriedAt=" + this.queriedAt + ", " + "eventualConsistencyNotice=" + this.eventualConsistencyNotice + ", " + "listener=" + this.listener + ", " + "summary=" + this.summary + ", " + "timeSeries=" + this.timeSeries + "]";
    }

    /**
     * Creates a new MailAnalyticsListenerOverview.
     *
     * @param startAt inclusive UTC start of the analytics interval
     * @param endAt exclusive UTC end of the analytics interval
     * @param bucketGranularity bucket granularity selected by the server
     * @param queriedAt UTC timestamp when the query was evaluated
     * @param eventualConsistencyNotice note about eventual consistency of the data
     * @param listener identity of the reported listener
     * @param summary summary metrics for the interval
     * @param timeSeries zero-filled buckets ordered oldest first
     */
    @JsonCreator
    public MailAnalyticsListenerOverview(String startAt, String endAt, MailAnalyticsBucketGranularity bucketGranularity, String queriedAt, String eventualConsistencyNotice, MailAnalyticsListenerIdentity listener, MailAnalyticsListenerOverviewMetrics summary, List<MailAnalyticsListenerOverviewTimePoint> timeSeries) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.bucketGranularity = bucketGranularity;
        this.queriedAt = queriedAt;
        this.eventualConsistencyNotice = eventualConsistencyNotice;
        this.listener = listener;
        this.summary = summary;
        this.timeSeries = timeSeries;
    }
}
