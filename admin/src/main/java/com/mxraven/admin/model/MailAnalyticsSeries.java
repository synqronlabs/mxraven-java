package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * Bounded additive analytics time series for one metric and dimension.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsSeries {
    private final String startAt;
    private final String endAt;
    private final String queriedAt;
    private final String eventualConsistencyNotice;
    private final MailAnalyticsBucketGranularity bucketGranularity;
    private final MailAnalyticsMetric metric;
    private final MailAnalyticsDimension dimension;
    private final int limit;
    private final List<MailAnalyticsSeriesItem> series;

    /** start of the queried window */
    public String startAt() {
        return startAt;
    }

    /** end of the queried window */
    public String endAt() {
        return endAt;
    }

    /** time the series was computed */
    public String queriedAt() {
        return queriedAt;
    }

    /** notice that recently ingested data may lag */
    public String eventualConsistencyNotice() {
        return eventualConsistencyNotice;
    }

    /** width of each time bucket */
    public MailAnalyticsBucketGranularity bucketGranularity() {
        return bucketGranularity;
    }

    /** metric aggregated by the series */
    public MailAnalyticsMetric metric() {
        return metric;
    }

    /** dimension used to split the metric */
    public MailAnalyticsDimension dimension() {
        return dimension;
    }

    /** maximum number of series items returned */
    public int limit() {
        return limit;
    }

    /** per-dimension series items */
    public List<MailAnalyticsSeriesItem> series() {
        return series;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsSeries that = (MailAnalyticsSeries) o;
        return Objects.equals(this.startAt, that.startAt)
                && Objects.equals(this.endAt, that.endAt)
                && Objects.equals(this.queriedAt, that.queriedAt)
                && Objects.equals(this.eventualConsistencyNotice, that.eventualConsistencyNotice)
                && Objects.equals(this.bucketGranularity, that.bucketGranularity)
                && Objects.equals(this.metric, that.metric)
                && Objects.equals(this.dimension, that.dimension)
                && this.limit == that.limit
                && Objects.equals(this.series, that.series);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.startAt, this.endAt, this.queriedAt, this.eventualConsistencyNotice, this.bucketGranularity, this.metric, this.dimension, this.limit, this.series);
    }

    @Override
    public String toString() {
        return "MailAnalyticsSeries[" + "startAt=" + this.startAt + ", " + "endAt=" + this.endAt + ", " + "queriedAt=" + this.queriedAt + ", " + "eventualConsistencyNotice=" + this.eventualConsistencyNotice + ", " + "bucketGranularity=" + this.bucketGranularity + ", " + "metric=" + this.metric + ", " + "dimension=" + this.dimension + ", " + "limit=" + this.limit + ", " + "series=" + this.series + "]";
    }

    /**
     * Creates a new MailAnalyticsSeries.
     *
     * @param startAt start of the queried window
     * @param endAt end of the queried window
     * @param queriedAt time the series was computed
     * @param eventualConsistencyNotice notice that recently ingested data may lag
     * @param bucketGranularity width of each time bucket
     * @param metric metric aggregated by the series
     * @param dimension dimension used to split the metric
     * @param limit maximum number of series items returned
     * @param series per-dimension series items
     */
    @JsonCreator
    public MailAnalyticsSeries(String startAt, String endAt, String queriedAt, String eventualConsistencyNotice, MailAnalyticsBucketGranularity bucketGranularity, MailAnalyticsMetric metric, MailAnalyticsDimension dimension, int limit, List<MailAnalyticsSeriesItem> series) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.queriedAt = queriedAt;
        this.eventualConsistencyNotice = eventualConsistencyNotice;
        this.bucketGranularity = bucketGranularity;
        this.metric = metric;
        this.dimension = dimension;
        this.limit = limit;
        this.series = series;
    }
}
