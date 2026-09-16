package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Terminal latency for a single terminal outcome.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsOutcomeLatency {
    private final MailAnalyticsOutcome outcome;
    private final MailAnalyticsLatencyMetric latency;

    /** terminal outcome */
    public MailAnalyticsOutcome outcome() {
        return outcome;
    }

    /** latency for the outcome */
    public MailAnalyticsLatencyMetric latency() {
        return latency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsOutcomeLatency that = (MailAnalyticsOutcomeLatency) o;
        return Objects.equals(this.outcome, that.outcome)
                && Objects.equals(this.latency, that.latency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.outcome, this.latency);
    }

    @Override
    public String toString() {
        return "MailAnalyticsOutcomeLatency[" + "outcome=" + this.outcome + ", " + "latency=" + this.latency + "]";
    }

    /**
     * Creates a new MailAnalyticsOutcomeLatency.
     *
     * @param outcome terminal outcome
     * @param latency latency for the outcome
     */
    @JsonCreator
    public MailAnalyticsOutcomeLatency(MailAnalyticsOutcome outcome, MailAnalyticsLatencyMetric latency) {
        this.outcome = outcome;
        this.latency = latency;
    }
}
