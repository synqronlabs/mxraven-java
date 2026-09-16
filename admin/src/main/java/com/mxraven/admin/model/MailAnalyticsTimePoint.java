package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * One zero-filled overview time-series bucket.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsTimePoint {
    private final String bucketStart;
    private final String bucketEnd;
    private final MailAnalyticsMetrics metrics;

    /** start of the bucket */
    public String bucketStart() {
        return bucketStart;
    }

    /** end of the bucket */
    public String bucketEnd() {
        return bucketEnd;
    }

    /** overview metrics for the bucket */
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
        MailAnalyticsTimePoint that = (MailAnalyticsTimePoint) o;
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
        return "MailAnalyticsTimePoint[" + "bucketStart=" + this.bucketStart + ", " + "bucketEnd=" + this.bucketEnd + ", " + "metrics=" + this.metrics + "]";
    }

    /**
     * Creates a new MailAnalyticsTimePoint.
     *
     * @param bucketStart start of the bucket
     * @param bucketEnd end of the bucket
     * @param metrics overview metrics for the bucket
     */
    @JsonCreator
    public MailAnalyticsTimePoint(String bucketStart, String bucketEnd, MailAnalyticsMetrics metrics) {
        this.bucketStart = bucketStart;
        this.bucketEnd = bucketEnd;
        this.metrics = metrics;
    }
}
