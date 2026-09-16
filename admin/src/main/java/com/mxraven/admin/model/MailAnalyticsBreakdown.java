package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * A bounded, ranked analytics breakdown grouping one additive metric by one
 * ledger dimension.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsBreakdown {
    private final String startAt;
    private final String endAt;
    private final String queriedAt;
    private final String eventualConsistencyNotice;
    private final MailAnalyticsMetric metric;
    private final MailAnalyticsDimension dimension;
    private final int limit;
    private final long total;
    private final List<MailAnalyticsBreakdownItem> items;
    private final long other;

    /** inclusive UTC start of the interval */
    public String startAt() {
        return startAt;
    }

    /** exclusive UTC end of the interval */
    public String endAt() {
        return endAt;
    }

    /** UTC timestamp when the query was evaluated */
    public String queriedAt() {
        return queriedAt;
    }

    /** note about eventual consistency of the data */
    public String eventualConsistencyNotice() {
        return eventualConsistencyNotice;
    }

    /** additive metric being grouped */
    public MailAnalyticsMetric metric() {
        return metric;
    }

    /** ledger dimension used to group the metric */
    public MailAnalyticsDimension dimension() {
        return dimension;
    }

    /** maximum ranked items requested */
    public int limit() {
        return limit;
    }

    /** total metric value across all groups */
    public long total() {
        return total;
    }

    /** ranked breakdown items */
    public List<MailAnalyticsBreakdownItem> items() {
        return items;
    }

    /** metric value not covered by the returned items */
    public long other() {
        return other;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsBreakdown that = (MailAnalyticsBreakdown) o;
        return Objects.equals(this.startAt, that.startAt)
                && Objects.equals(this.endAt, that.endAt)
                && Objects.equals(this.queriedAt, that.queriedAt)
                && Objects.equals(this.eventualConsistencyNotice, that.eventualConsistencyNotice)
                && Objects.equals(this.metric, that.metric)
                && Objects.equals(this.dimension, that.dimension)
                && this.limit == that.limit
                && this.total == that.total
                && Objects.equals(this.items, that.items)
                && this.other == that.other;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.startAt, this.endAt, this.queriedAt, this.eventualConsistencyNotice, this.metric, this.dimension, this.limit, this.total, this.items, this.other);
    }

    @Override
    public String toString() {
        return "MailAnalyticsBreakdown[" + "startAt=" + this.startAt + ", " + "endAt=" + this.endAt + ", " + "queriedAt=" + this.queriedAt + ", " + "eventualConsistencyNotice=" + this.eventualConsistencyNotice + ", " + "metric=" + this.metric + ", " + "dimension=" + this.dimension + ", " + "limit=" + this.limit + ", " + "total=" + this.total + ", " + "items=" + this.items + ", " + "other=" + this.other + "]";
    }

    /**
     * Creates a new MailAnalyticsBreakdown.
     *
     * @param startAt inclusive UTC start of the interval
     * @param endAt exclusive UTC end of the interval
     * @param queriedAt UTC timestamp when the query was evaluated
     * @param eventualConsistencyNotice note about eventual consistency of the data
     * @param metric additive metric being grouped
     * @param dimension ledger dimension used to group the metric
     * @param limit maximum ranked items requested
     * @param total total metric value across all groups
     * @param items ranked breakdown items
     * @param other metric value not covered by the returned items
     */
    @JsonCreator
    public MailAnalyticsBreakdown(String startAt, String endAt, String queriedAt, String eventualConsistencyNotice, MailAnalyticsMetric metric, MailAnalyticsDimension dimension, int limit, long total, List<MailAnalyticsBreakdownItem> items, long other) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.queriedAt = queriedAt;
        this.eventualConsistencyNotice = eventualConsistencyNotice;
        this.metric = metric;
        this.dimension = dimension;
        this.limit = limit;
        this.total = total;
        this.items = items;
        this.other = other;
    }
}
