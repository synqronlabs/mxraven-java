package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * One interval in a comparison response.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsComparisonPeriod {
    private final String startAt;
    private final String endAt;
    private final MailAnalyticsMetrics metrics;

    /** inclusive UTC start of the interval */
    public String startAt() {
        return startAt;
    }

    /** exclusive UTC end of the interval */
    public String endAt() {
        return endAt;
    }

    /** summary metrics for the interval */
    public MailAnalyticsMetrics metrics() {
        return metrics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsComparisonPeriod that = (MailAnalyticsComparisonPeriod) o;
        return Objects.equals(this.startAt, that.startAt)
                && Objects.equals(this.endAt, that.endAt)
                && Objects.equals(this.metrics, that.metrics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.startAt, this.endAt, this.metrics);
    }

    @Override
    public String toString() {
        return "MailAnalyticsComparisonPeriod[" + "startAt=" + this.startAt + ", " + "endAt=" + this.endAt + ", " + "metrics=" + this.metrics + "]";
    }

    /**
     * Creates a new MailAnalyticsComparisonPeriod.
     *
     * @param startAt inclusive UTC start of the interval
     * @param endAt exclusive UTC end of the interval
     * @param metrics summary metrics for the interval
     */
    @JsonCreator
    public MailAnalyticsComparisonPeriod(String startAt, String endAt, MailAnalyticsMetrics metrics) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.metrics = metrics;
    }
}
