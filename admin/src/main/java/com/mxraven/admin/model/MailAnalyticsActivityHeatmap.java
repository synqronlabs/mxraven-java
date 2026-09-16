package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * UTC task activity heatmap with exactly 168 zero-filled weekday/hour cells.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsActivityHeatmap {
    private final String startAt;
    private final String endAt;
    private final String queriedAt;
    private final String eventualConsistencyNotice;
    private final MailAnalyticsTimezone timezone;
    private final MailAnalyticsMetric metric;
    private final List<MailAnalyticsActivityHeatmapCell> cells;

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

    /** timezone used to bucket the heatmap */
    public MailAnalyticsTimezone timezone() {
        return timezone;
    }

    /** metric plotted in the heatmap */
    public MailAnalyticsMetric metric() {
        return metric;
    }

    /** the zero-filled weekday/hour cells */
    public List<MailAnalyticsActivityHeatmapCell> cells() {
        return cells;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsActivityHeatmap that = (MailAnalyticsActivityHeatmap) o;
        return Objects.equals(this.startAt, that.startAt)
                && Objects.equals(this.endAt, that.endAt)
                && Objects.equals(this.queriedAt, that.queriedAt)
                && Objects.equals(this.eventualConsistencyNotice, that.eventualConsistencyNotice)
                && Objects.equals(this.timezone, that.timezone)
                && Objects.equals(this.metric, that.metric)
                && Objects.equals(this.cells, that.cells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.startAt, this.endAt, this.queriedAt, this.eventualConsistencyNotice, this.timezone, this.metric, this.cells);
    }

    @Override
    public String toString() {
        return "MailAnalyticsActivityHeatmap[" + "startAt=" + this.startAt + ", " + "endAt=" + this.endAt + ", " + "queriedAt=" + this.queriedAt + ", " + "eventualConsistencyNotice=" + this.eventualConsistencyNotice + ", " + "timezone=" + this.timezone + ", " + "metric=" + this.metric + ", " + "cells=" + this.cells + "]";
    }

    /**
     * Creates a new MailAnalyticsActivityHeatmap.
     *
     * @param startAt inclusive UTC start of the interval
     * @param endAt exclusive UTC end of the interval
     * @param queriedAt UTC timestamp when the query was evaluated
     * @param eventualConsistencyNotice note about eventual consistency of the data
     * @param timezone timezone used to bucket the heatmap
     * @param metric metric plotted in the heatmap
     * @param cells the zero-filled weekday/hour cells
     */
    @JsonCreator
    public MailAnalyticsActivityHeatmap(String startAt, String endAt, String queriedAt, String eventualConsistencyNotice, MailAnalyticsTimezone timezone, MailAnalyticsMetric metric, List<MailAnalyticsActivityHeatmapCell> cells) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.queriedAt = queriedAt;
        this.eventualConsistencyNotice = eventualConsistencyNotice;
        this.timezone = timezone;
        this.metric = metric;
        this.cells = cells;
    }
}
