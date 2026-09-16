package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Overview metrics for the requested interval and the immediately preceding
 * interval of equal duration.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsComparison {
    private final String queriedAt;
    private final String eventualConsistencyNotice;
    private final MailAnalyticsComparisonPeriod current;
    private final MailAnalyticsComparisonPeriod preceding;

    /** UTC timestamp when the query was evaluated */
    public String queriedAt() {
        return queriedAt;
    }

    /** note about eventual consistency of the data */
    public String eventualConsistencyNotice() {
        return eventualConsistencyNotice;
    }

    /** metrics for the requested interval */
    public MailAnalyticsComparisonPeriod current() {
        return current;
    }

    /** metrics for the immediately preceding interval */
    public MailAnalyticsComparisonPeriod preceding() {
        return preceding;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsComparison that = (MailAnalyticsComparison) o;
        return Objects.equals(this.queriedAt, that.queriedAt)
                && Objects.equals(this.eventualConsistencyNotice, that.eventualConsistencyNotice)
                && Objects.equals(this.current, that.current)
                && Objects.equals(this.preceding, that.preceding);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.queriedAt, this.eventualConsistencyNotice, this.current, this.preceding);
    }

    @Override
    public String toString() {
        return "MailAnalyticsComparison[" + "queriedAt=" + this.queriedAt + ", " + "eventualConsistencyNotice=" + this.eventualConsistencyNotice + ", " + "current=" + this.current + ", " + "preceding=" + this.preceding + "]";
    }

    /**
     * Creates a new MailAnalyticsComparison.
     *
     * @param queriedAt UTC timestamp when the query was evaluated
     * @param eventualConsistencyNotice note about eventual consistency of the data
     * @param current metrics for the requested interval
     * @param preceding metrics for the immediately preceding interval
     */
    @JsonCreator
    public MailAnalyticsComparison(String queriedAt, String eventualConsistencyNotice, MailAnalyticsComparisonPeriod current, MailAnalyticsComparisonPeriod preceding) {
        this.queriedAt = queriedAt;
        this.eventualConsistencyNotice = eventualConsistencyNotice;
        this.current = current;
        this.preceding = preceding;
    }
}
