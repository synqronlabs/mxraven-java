package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Size statistics for one UTC bucket. A zero sample count means the statistic
 * values are placeholders rather than observations.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsTaskSizeBucket {
    private final String bucketStart;
    private final String bucketEnd;
    private final long sampleCount;
    private final double averageBytes;
    private final double p50Bytes;
    private final double p90Bytes;
    private final double p95Bytes;
    private final double p99Bytes;

    /** start of the UTC bucket */
    public String bucketStart() {
        return bucketStart;
    }

    /** end of the UTC bucket */
    public String bucketEnd() {
        return bucketEnd;
    }

    /** number of message sizes observed */
    public long sampleCount() {
        return sampleCount;
    }

    /** mean message size in bytes */
    public double averageBytes() {
        return averageBytes;
    }

    /** 50th percentile message size in bytes */
    public double p50Bytes() {
        return p50Bytes;
    }

    /** 90th percentile message size in bytes */
    public double p90Bytes() {
        return p90Bytes;
    }

    /** 95th percentile message size in bytes */
    public double p95Bytes() {
        return p95Bytes;
    }

    /** 99th percentile message size in bytes */
    public double p99Bytes() {
        return p99Bytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsTaskSizeBucket that = (MailAnalyticsTaskSizeBucket) o;
        return Objects.equals(this.bucketStart, that.bucketStart)
                && Objects.equals(this.bucketEnd, that.bucketEnd)
                && this.sampleCount == that.sampleCount
                && Double.compare(this.averageBytes, that.averageBytes) == 0
                && Double.compare(this.p50Bytes, that.p50Bytes) == 0
                && Double.compare(this.p90Bytes, that.p90Bytes) == 0
                && Double.compare(this.p95Bytes, that.p95Bytes) == 0
                && Double.compare(this.p99Bytes, that.p99Bytes) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.bucketStart, this.bucketEnd, this.sampleCount, this.averageBytes, this.p50Bytes, this.p90Bytes, this.p95Bytes, this.p99Bytes);
    }

    @Override
    public String toString() {
        return "MailAnalyticsTaskSizeBucket[" + "bucketStart=" + this.bucketStart + ", " + "bucketEnd=" + this.bucketEnd + ", " + "sampleCount=" + this.sampleCount + ", " + "averageBytes=" + this.averageBytes + ", " + "p50Bytes=" + this.p50Bytes + ", " + "p90Bytes=" + this.p90Bytes + ", " + "p95Bytes=" + this.p95Bytes + ", " + "p99Bytes=" + this.p99Bytes + "]";
    }

    /**
     * Creates a new MailAnalyticsTaskSizeBucket.
     *
     * @param bucketStart start of the UTC bucket
     * @param bucketEnd end of the UTC bucket
     * @param sampleCount number of message sizes observed
     * @param averageBytes mean message size in bytes
     * @param p50Bytes 50th percentile message size in bytes
     * @param p90Bytes 90th percentile message size in bytes
     * @param p95Bytes 95th percentile message size in bytes
     * @param p99Bytes 99th percentile message size in bytes
     */
    @JsonCreator
    public MailAnalyticsTaskSizeBucket(String bucketStart, String bucketEnd, long sampleCount, double averageBytes, double p50Bytes, double p90Bytes, double p95Bytes, double p99Bytes) {
        this.bucketStart = bucketStart;
        this.bucketEnd = bucketEnd;
        this.sampleCount = sampleCount;
        this.averageBytes = averageBytes;
        this.p50Bytes = p50Bytes;
        this.p90Bytes = p90Bytes;
        this.p95Bytes = p95Bytes;
        this.p99Bytes = p99Bytes;
    }
}
