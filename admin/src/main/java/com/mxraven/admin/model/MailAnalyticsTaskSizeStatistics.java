package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * Zero-filled UTC buckets with averages and approximate t-digest percentiles
 * over task-created message sizes.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsTaskSizeStatistics {
    private final String startAt;
    private final String endAt;
    private final String queriedAt;
    private final String eventualConsistencyNotice;
    private final MailAnalyticsBucketGranularity bucketGranularity;
    private final List<MailAnalyticsTaskSizeBucket> buckets;

    /** start of the queried window */
    public String startAt() {
        return startAt;
    }

    /** end of the queried window */
    public String endAt() {
        return endAt;
    }

    /** time the statistics were computed */
    public String queriedAt() {
        return queriedAt;
    }

    /** notice that recently ingested data may lag */
    public String eventualConsistencyNotice() {
        return eventualConsistencyNotice;
    }

    /** width of each time bucket */
    public MailAnalyticsBucketGranularity bucketGranularity() {
        return bucketGranularity;
    }

    /** per-bucket size statistics */
    public List<MailAnalyticsTaskSizeBucket> buckets() {
        return buckets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsTaskSizeStatistics that = (MailAnalyticsTaskSizeStatistics) o;
        return Objects.equals(this.startAt, that.startAt)
                && Objects.equals(this.endAt, that.endAt)
                && Objects.equals(this.queriedAt, that.queriedAt)
                && Objects.equals(this.eventualConsistencyNotice, that.eventualConsistencyNotice)
                && Objects.equals(this.bucketGranularity, that.bucketGranularity)
                && Objects.equals(this.buckets, that.buckets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.startAt, this.endAt, this.queriedAt, this.eventualConsistencyNotice, this.bucketGranularity, this.buckets);
    }

    @Override
    public String toString() {
        return "MailAnalyticsTaskSizeStatistics[" + "startAt=" + this.startAt + ", " + "endAt=" + this.endAt + ", " + "queriedAt=" + this.queriedAt + ", " + "eventualConsistencyNotice=" + this.eventualConsistencyNotice + ", " + "bucketGranularity=" + this.bucketGranularity + ", " + "buckets=" + this.buckets + "]";
    }

    /**
     * Creates a new MailAnalyticsTaskSizeStatistics.
     *
     * @param startAt start of the queried window
     * @param endAt end of the queried window
     * @param queriedAt time the statistics were computed
     * @param eventualConsistencyNotice notice that recently ingested data may lag
     * @param bucketGranularity width of each time bucket
     * @param buckets per-bucket size statistics
     */
    @JsonCreator
    public MailAnalyticsTaskSizeStatistics(String startAt, String endAt, String queriedAt, String eventualConsistencyNotice, MailAnalyticsBucketGranularity bucketGranularity, List<MailAnalyticsTaskSizeBucket> buckets) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.queriedAt = queriedAt;
        this.eventualConsistencyNotice = eventualConsistencyNotice;
        this.bucketGranularity = bucketGranularity;
        this.buckets = buckets;
    }
}
