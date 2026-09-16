package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * A latency metric with sample count, percentiles, and a count distribution.
 *
 * <p>Percentiles are null when there are no samples.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsLatencyMetric {
    private final long sampleCount;
    private final Double p50Ms;
    private final Double p90Ms;
    private final Double p95Ms;
    private final Double p99Ms;
    private final List<MailAnalyticsCountBin> distribution;

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

    /** count distribution of the samples */
    public List<MailAnalyticsCountBin> distribution() {
        return distribution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsLatencyMetric that = (MailAnalyticsLatencyMetric) o;
        return this.sampleCount == that.sampleCount
                && Objects.equals(this.p50Ms, that.p50Ms)
                && Objects.equals(this.p90Ms, that.p90Ms)
                && Objects.equals(this.p95Ms, that.p95Ms)
                && Objects.equals(this.p99Ms, that.p99Ms)
                && Objects.equals(this.distribution, that.distribution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.sampleCount, this.p50Ms, this.p90Ms, this.p95Ms, this.p99Ms, this.distribution);
    }

    @Override
    public String toString() {
        return "MailAnalyticsLatencyMetric[" + "sampleCount=" + this.sampleCount + ", " + "p50Ms=" + this.p50Ms + ", " + "p90Ms=" + this.p90Ms + ", " + "p95Ms=" + this.p95Ms + ", " + "p99Ms=" + this.p99Ms + ", " + "distribution=" + this.distribution + "]";
    }

    /**
     * Creates a new MailAnalyticsLatencyMetric.
     *
     * @param sampleCount number of samples
     * @param p50Ms 50th-percentile latency in milliseconds, or {@code null} when there are no samples
     * @param p90Ms 90th-percentile latency in milliseconds, or {@code null} when there are no samples
     * @param p95Ms 95th-percentile latency in milliseconds, or {@code null} when there are no samples
     * @param p99Ms 99th-percentile latency in milliseconds, or {@code null} when there are no samples
     * @param distribution count distribution of the samples
     */
    @JsonCreator
    public MailAnalyticsLatencyMetric(long sampleCount, Double p50Ms, Double p90Ms, Double p95Ms, Double p99Ms, List<MailAnalyticsCountBin> distribution) {
        this.sampleCount = sampleCount;
        this.p50Ms = p50Ms;
        this.p90Ms = p90Ms;
        this.p95Ms = p95Ms;
        this.p99Ms = p99Ms;
        this.distribution = distribution;
    }
}
