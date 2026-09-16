package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Terminal-outcome latency for one message-size bin.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsLatencyBySize {
    private final MailAnalyticsSizeBin sizeBin;
    private final long sampleCount;
    private final MailAnalyticsLatencyStatistic terminalOutcomeLatency;

    /** message-size bin */
    public MailAnalyticsSizeBin sizeBin() {
        return sizeBin;
    }

    /** number of samples in the bin */
    public long sampleCount() {
        return sampleCount;
    }

    /** terminal-outcome latency for the bin */
    public MailAnalyticsLatencyStatistic terminalOutcomeLatency() {
        return terminalOutcomeLatency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsLatencyBySize that = (MailAnalyticsLatencyBySize) o;
        return Objects.equals(this.sizeBin, that.sizeBin)
                && this.sampleCount == that.sampleCount
                && Objects.equals(this.terminalOutcomeLatency, that.terminalOutcomeLatency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.sizeBin, this.sampleCount, this.terminalOutcomeLatency);
    }

    @Override
    public String toString() {
        return "MailAnalyticsLatencyBySize[" + "sizeBin=" + this.sizeBin + ", " + "sampleCount=" + this.sampleCount + ", " + "terminalOutcomeLatency=" + this.terminalOutcomeLatency + "]";
    }

    /**
     * Creates a new MailAnalyticsLatencyBySize.
     *
     * @param sizeBin message-size bin
     * @param sampleCount number of samples in the bin
     * @param terminalOutcomeLatency terminal-outcome latency for the bin
     */
    @JsonCreator
    public MailAnalyticsLatencyBySize(MailAnalyticsSizeBin sizeBin, long sampleCount, MailAnalyticsLatencyStatistic terminalOutcomeLatency) {
        this.sizeBin = sizeBin;
        this.sampleCount = sampleCount;
        this.terminalOutcomeLatency = terminalOutcomeLatency;
    }
}
