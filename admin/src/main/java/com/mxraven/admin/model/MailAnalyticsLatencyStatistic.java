package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * A latency statistic with sample count and percentiles.
 *
 * <p>Percentiles are null when there are no samples.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsLatencyStatistic {
    private final long sampleCount;
    private final Double p50Ms;
    private final Double p90Ms;
    private final Double p95Ms;
    private final Double p99Ms;

    /** number of samples */
    public long sampleCount() {
        return sampleCount;
    }

    /** 50th-percentile latency in milliseconds, or {@code null} when there are no samples */
    public Double p50Ms() {
        return p50Ms;
    }

    /** 90th-percentile latency in milliseconds, or {@code null} when there are no samples */
    public Double p90Ms() {
        return p90Ms;
    }

    /** 95th-percentile latency in milliseconds, or {@code null} when there are no samples */
    public Double p95Ms() {
        return p95Ms;
    }

    /** 99th-percentile latency in milliseconds, or {@code null} when there are no samples */
    public Double p99Ms() {
        return p99Ms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsLatencyStatistic that = (MailAnalyticsLatencyStatistic) o;
        return this.sampleCount == that.sampleCount
                && Objects.equals(this.p50Ms, that.p50Ms)
                && Objects.equals(this.p90Ms, that.p90Ms)
                && Objects.equals(this.p95Ms, that.p95Ms)
                && Objects.equals(this.p99Ms, that.p99Ms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.sampleCount, this.p50Ms, this.p90Ms, this.p95Ms, this.p99Ms);
    }

    @Override
    public String toString() {
        return "MailAnalyticsLatencyStatistic[" + "sampleCount=" + this.sampleCount + ", " + "p50Ms=" + this.p50Ms + ", " + "p90Ms=" + this.p90Ms + ", " + "p95Ms=" + this.p95Ms + ", " + "p99Ms=" + this.p99Ms + "]";
    }

    /**
     * Creates a new MailAnalyticsLatencyStatistic.
     *
     * @param sampleCount number of samples
     * @param p50Ms 50th-percentile latency in milliseconds, or {@code null} when there are no samples
     * @param p90Ms 90th-percentile latency in milliseconds, or {@code null} when there are no samples
     * @param p95Ms 95th-percentile latency in milliseconds, or {@code null} when there are no samples
     * @param p99Ms 99th-percentile latency in milliseconds, or {@code null} when there are no samples
     */
    @JsonCreator
    public MailAnalyticsLatencyStatistic(long sampleCount, Double p50Ms, Double p90Ms, Double p95Ms, Double p99Ms) {
        this.sampleCount = sampleCount;
        this.p50Ms = p50Ms;
        this.p90Ms = p90Ms;
        this.p95Ms = p95Ms;
        this.p99Ms = p99Ms;
    }
}
