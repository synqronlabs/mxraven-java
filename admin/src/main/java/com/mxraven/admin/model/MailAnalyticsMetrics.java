package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Fixed Phase 1 mail analytics summary metrics for an interval.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsMetrics {
    private final MailAnalyticsAcceptedMessages acceptedMessages;
    private final long egressTasks;
    private final long dataBytes;
    private final long billableUnits;
    private final MailAnalyticsTerminalOutcomes terminalOutcomes;
    private final MailAnalyticsRate deliverySuccessRate;
    private final MailAnalyticsSMTPResponses smtpResponses;
    private final MailAnalyticsFeedback feedback;
    private final long suppressionsApplied;

    /** accepted message counts by channel */
    public MailAnalyticsAcceptedMessages acceptedMessages() {
        return acceptedMessages;
    }

    /** number of egress tasks */
    public long egressTasks() {
        return egressTasks;
    }

    /** egress data bytes */
    public long dataBytes() {
        return dataBytes;
    }

    /** billable units */
    public long billableUnits() {
        return billableUnits;
    }

    /** terminal outcome counts */
    public MailAnalyticsTerminalOutcomes terminalOutcomes() {
        return terminalOutcomes;
    }

    /** successful-delivery rate */
    public MailAnalyticsRate deliverySuccessRate() {
        return deliverySuccessRate;
    }

    /** SMTP response counts */
    public MailAnalyticsSMTPResponses smtpResponses() {
        return smtpResponses;
    }

    /** feedback fact counts */
    public MailAnalyticsFeedback feedback() {
        return feedback;
    }

    /** suppressions applied */
    public long suppressionsApplied() {
        return suppressionsApplied;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsMetrics that = (MailAnalyticsMetrics) o;
        return Objects.equals(this.acceptedMessages, that.acceptedMessages)
                && this.egressTasks == that.egressTasks
                && this.dataBytes == that.dataBytes
                && this.billableUnits == that.billableUnits
                && Objects.equals(this.terminalOutcomes, that.terminalOutcomes)
                && Objects.equals(this.deliverySuccessRate, that.deliverySuccessRate)
                && Objects.equals(this.smtpResponses, that.smtpResponses)
                && Objects.equals(this.feedback, that.feedback)
                && this.suppressionsApplied == that.suppressionsApplied;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.acceptedMessages, this.egressTasks, this.dataBytes, this.billableUnits, this.terminalOutcomes, this.deliverySuccessRate, this.smtpResponses, this.feedback, this.suppressionsApplied);
    }

    @Override
    public String toString() {
        return "MailAnalyticsMetrics[" + "acceptedMessages=" + this.acceptedMessages + ", " + "egressTasks=" + this.egressTasks + ", " + "dataBytes=" + this.dataBytes + ", " + "billableUnits=" + this.billableUnits + ", " + "terminalOutcomes=" + this.terminalOutcomes + ", " + "deliverySuccessRate=" + this.deliverySuccessRate + ", " + "smtpResponses=" + this.smtpResponses + ", " + "feedback=" + this.feedback + ", " + "suppressionsApplied=" + this.suppressionsApplied + "]";
    }

    /**
     * Creates a new MailAnalyticsMetrics.
     *
     * @param acceptedMessages accepted message counts by channel
     * @param egressTasks number of egress tasks
     * @param dataBytes egress data bytes
     * @param billableUnits billable units
     * @param terminalOutcomes terminal outcome counts
     * @param deliverySuccessRate successful-delivery rate
     * @param smtpResponses SMTP response counts
     * @param feedback feedback fact counts
     * @param suppressionsApplied suppressions applied
     */
    @JsonCreator
    public MailAnalyticsMetrics(MailAnalyticsAcceptedMessages acceptedMessages, long egressTasks, long dataBytes, long billableUnits, MailAnalyticsTerminalOutcomes terminalOutcomes, MailAnalyticsRate deliverySuccessRate, MailAnalyticsSMTPResponses smtpResponses, MailAnalyticsFeedback feedback, long suppressionsApplied) {
        this.acceptedMessages = acceptedMessages;
        this.egressTasks = egressTasks;
        this.dataBytes = dataBytes;
        this.billableUnits = billableUnits;
        this.terminalOutcomes = terminalOutcomes;
        this.deliverySuccessRate = deliverySuccessRate;
        this.smtpResponses = smtpResponses;
        this.feedback = feedback;
        this.suppressionsApplied = suppressionsApplied;
    }
}
