package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * One dimension value and its zero-filled points within a series.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsSeriesItem {
    private final String key;
    private final String displayName;
    private final MailAnalyticsResourceState resourceState;
    private final List<MailAnalyticsSeriesPoint> points;

    /** dimension value identifying this series item */
    public String key() {
        return key;
    }

    /** human-readable label for the dimension value */
    public String displayName() {
        return displayName;
    }

    /** state of the referenced resource, when applicable */
    public MailAnalyticsResourceState resourceState() {
        return resourceState;
    }

    /** zero-filled points for this dimension value */
    public List<MailAnalyticsSeriesPoint> points() {
        return points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsSeriesItem that = (MailAnalyticsSeriesItem) o;
        return Objects.equals(this.key, that.key)
                && Objects.equals(this.displayName, that.displayName)
                && Objects.equals(this.resourceState, that.resourceState)
                && Objects.equals(this.points, that.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key, this.displayName, this.resourceState, this.points);
    }

    @Override
    public String toString() {
        return "MailAnalyticsSeriesItem[" + "key=" + this.key + ", " + "displayName=" + this.displayName + ", " + "resourceState=" + this.resourceState + ", " + "points=" + this.points + "]";
    }

    /**
     * Creates a new MailAnalyticsSeriesItem.
     *
     * @param key dimension value identifying this series item
     * @param displayName human-readable label for the dimension value
     * @param resourceState state of the referenced resource, when applicable
     * @param points zero-filled points for this dimension value
     */
    @JsonCreator
    public MailAnalyticsSeriesItem(String key, String displayName, MailAnalyticsResourceState resourceState, List<MailAnalyticsSeriesPoint> points) {
        this.key = key;
        this.displayName = displayName;
        this.resourceState = resourceState;
        this.points = points;
    }
}
