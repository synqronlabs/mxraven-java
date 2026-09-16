package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * Bounded listener breakdown of egress task volume for an overview interval.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsListenerBreakdown {
    private final int limit;
    private final long totalEgressTasks;
    private final List<MailAnalyticsListenerBreakdownItem> items;
    private final long otherEgressTasks;

    /** maximum ranked listeners requested */
    public int limit() {
        return limit;
    }

    /** total egress tasks across all listeners */
    public long totalEgressTasks() {
        return totalEgressTasks;
    }

    /** ranked listener items */
    public List<MailAnalyticsListenerBreakdownItem> items() {
        return items;
    }

    /** egress tasks not covered by the returned items */
    public long otherEgressTasks() {
        return otherEgressTasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsListenerBreakdown that = (MailAnalyticsListenerBreakdown) o;
        return this.limit == that.limit
                && this.totalEgressTasks == that.totalEgressTasks
                && Objects.equals(this.items, that.items)
                && this.otherEgressTasks == that.otherEgressTasks;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.limit, this.totalEgressTasks, this.items, this.otherEgressTasks);
    }

    @Override
    public String toString() {
        return "MailAnalyticsListenerBreakdown[" + "limit=" + this.limit + ", " + "totalEgressTasks=" + this.totalEgressTasks + ", " + "items=" + this.items + ", " + "otherEgressTasks=" + this.otherEgressTasks + "]";
    }

    /**
     * Creates a new MailAnalyticsListenerBreakdown.
     *
     * @param limit maximum ranked listeners requested
     * @param totalEgressTasks total egress tasks across all listeners
     * @param items ranked listener items
     * @param otherEgressTasks egress tasks not covered by the returned items
     */
    @JsonCreator
    public MailAnalyticsListenerBreakdown(int limit, long totalEgressTasks, List<MailAnalyticsListenerBreakdownItem> items, long otherEgressTasks) {
        this.limit = limit;
        this.totalEgressTasks = totalEgressTasks;
        this.items = items;
        this.otherEgressTasks = otherEgressTasks;
    }
}
