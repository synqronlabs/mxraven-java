package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * One bucket value within a series item.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsSeriesPoint {
    private final String bucketStart;
    private final String bucketEnd;
    private final long value;

    /** start of the bucket */
    public String bucketStart() {
        return bucketStart;
    }

    /** end of the bucket */
    public String bucketEnd() {
        return bucketEnd;
    }

    /** aggregated value for the bucket */
    public long value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsSeriesPoint that = (MailAnalyticsSeriesPoint) o;
        return Objects.equals(this.bucketStart, that.bucketStart)
                && Objects.equals(this.bucketEnd, that.bucketEnd)
                && this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.bucketStart, this.bucketEnd, this.value);
    }

    @Override
    public String toString() {
        return "MailAnalyticsSeriesPoint[" + "bucketStart=" + this.bucketStart + ", " + "bucketEnd=" + this.bucketEnd + ", " + "value=" + this.value + "]";
    }

    /**
     * Creates a new MailAnalyticsSeriesPoint.
     *
     * @param bucketStart start of the bucket
     * @param bucketEnd end of the bucket
     * @param value aggregated value for the bucket
     */
    @JsonCreator
    public MailAnalyticsSeriesPoint(String bucketStart, String bucketEnd, long value) {
        this.bucketStart = bucketStart;
        this.bucketEnd = bucketEnd;
        this.value = value;
    }
}
