package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * One zero-filled UTC time-series bucket of a listener overview.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsListenerOverviewTimePoint {
    private final String bucketStart;
    private final String bucketEnd;
    private final MailAnalyticsListenerOverviewMetrics metrics;

    /** inclusive UTC start of the bucket */
    public String bucketStart() {
        return bucketStart;
    }

    /** exclusive UTC end of the bucket */
    public String bucketEnd() {
        return bucketEnd;
    }

    /** metrics for the bucket */
    public MailAnalyticsListenerOverviewMetrics metrics() {
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
        MailAnalyticsListenerOverviewTimePoint that = (MailAnalyticsListenerOverviewTimePoint) o;
        return Objects.equals(this.bucketStart, that.bucketStart)
                && Objects.equals(this.bucketEnd, that.bucketEnd)
                && Objects.equals(this.metrics, that.metrics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.bucketStart, this.bucketEnd, this.metrics);
    }

    @Override
    public String toString() {
        return "MailAnalyticsListenerOverviewTimePoint[" + "bucketStart=" + this.bucketStart + ", " + "bucketEnd=" + this.bucketEnd + ", " + "metrics=" + this.metrics + "]";
    }

    /**
     * Creates a new MailAnalyticsListenerOverviewTimePoint.
     *
     * @param bucketStart inclusive UTC start of the bucket
     * @param bucketEnd exclusive UTC end of the bucket
     * @param metrics metrics for the bucket
     */
    @JsonCreator
    public MailAnalyticsListenerOverviewTimePoint(String bucketStart, String bucketEnd, MailAnalyticsListenerOverviewMetrics metrics) {
        this.bucketStart = bucketStart;
        this.bucketEnd = bucketEnd;
        this.metrics = metrics;
    }
}
