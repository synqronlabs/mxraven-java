package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * One weekday/hour heatmap cell. Weekdays use ISO numbering where Monday is 1
 * and Sunday is 7.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsActivityHeatmapCell {
    private final int isoWeekday;
    private final int hour;
    private final long egressTasks;

    /** ISO weekday number, Monday {@code 1} through Sunday {@code 7} */
    public int isoWeekday() {
        return isoWeekday;
    }

    /** UTC hour of day, {@code 0} through {@code 23} */
    public int hour() {
        return hour;
    }

    /** egress tasks observed in this weekday/hour cell */
    public long egressTasks() {
        return egressTasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsActivityHeatmapCell that = (MailAnalyticsActivityHeatmapCell) o;
        return this.isoWeekday == that.isoWeekday
                && this.hour == that.hour
                && this.egressTasks == that.egressTasks;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.isoWeekday, this.hour, this.egressTasks);
    }

    @Override
    public String toString() {
        return "MailAnalyticsActivityHeatmapCell[" + "isoWeekday=" + this.isoWeekday + ", " + "hour=" + this.hour + ", " + "egressTasks=" + this.egressTasks + "]";
    }

    /**
     * Creates a new MailAnalyticsActivityHeatmapCell.
     *
     * @param isoWeekday ISO weekday number, Monday {@code 1} through Sunday {@code 7}
     * @param hour UTC hour of day, {@code 0} through {@code 23}
     * @param egressTasks egress tasks observed in this weekday/hour cell
     */
    @JsonCreator
    public MailAnalyticsActivityHeatmapCell(int isoWeekday, int hour, long egressTasks) {
        this.isoWeekday = isoWeekday;
        this.hour = hour;
        this.egressTasks = egressTasks;
    }
}
