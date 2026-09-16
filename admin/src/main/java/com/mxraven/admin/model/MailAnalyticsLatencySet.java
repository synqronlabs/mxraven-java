package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * Observed latency metrics across first attempt, successful delivery, and
 * terminal outcomes.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsLatencySet {
    private final MailAnalyticsLatencyMetric firstAttempt;
    private final MailAnalyticsLatencyMetric successfulDelivery;
    private final MailAnalyticsLatencyMetric terminalOutcome;
    private final List<MailAnalyticsOutcomeLatency> terminalByOutcome;

    /** latency of first delivery attempts */
    public MailAnalyticsLatencyMetric firstAttempt() {
        return firstAttempt;
    }

    /** latency of successful deliveries */
    public MailAnalyticsLatencyMetric successfulDelivery() {
        return successfulDelivery;
    }

    /** latency of terminal outcomes */
    public MailAnalyticsLatencyMetric terminalOutcome() {
        return terminalOutcome;
    }

    /** terminal latency per outcome */
    public List<MailAnalyticsOutcomeLatency> terminalByOutcome() {
        return terminalByOutcome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsLatencySet that = (MailAnalyticsLatencySet) o;
        return Objects.equals(this.firstAttempt, that.firstAttempt)
                && Objects.equals(this.successfulDelivery, that.successfulDelivery)
                && Objects.equals(this.terminalOutcome, that.terminalOutcome)
                && Objects.equals(this.terminalByOutcome, that.terminalByOutcome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.firstAttempt, this.successfulDelivery, this.terminalOutcome, this.terminalByOutcome);
    }

    @Override
    public String toString() {
        return "MailAnalyticsLatencySet[" + "firstAttempt=" + this.firstAttempt + ", " + "successfulDelivery=" + this.successfulDelivery + ", " + "terminalOutcome=" + this.terminalOutcome + ", " + "terminalByOutcome=" + this.terminalByOutcome + "]";
    }

    /**
     * Creates a new MailAnalyticsLatencySet.
     *
     * @param firstAttempt latency of first delivery attempts
     * @param successfulDelivery latency of successful deliveries
     * @param terminalOutcome latency of terminal outcomes
     * @param terminalByOutcome terminal latency per outcome
     */
    @JsonCreator
    public MailAnalyticsLatencySet(MailAnalyticsLatencyMetric firstAttempt, MailAnalyticsLatencyMetric successfulDelivery, MailAnalyticsLatencyMetric terminalOutcome, List<MailAnalyticsOutcomeLatency> terminalByOutcome) {
        this.firstAttempt = firstAttempt;
        this.successfulDelivery = successfulDelivery;
        this.terminalOutcome = terminalOutcome;
        this.terminalByOutcome = terminalByOutcome;
    }
}
