package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Summary metrics of one listener for the requested interval.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsListenerOverviewMetrics {
    private final MailAnalyticsAcceptedMessages acceptedMessages;
    private final long egressTasks;
    private final MailAnalyticsRate deliverySuccessRate;
    private final MailAnalyticsSMTPResponses smtpResponses;

    /** accepted message counts split by channel */
    public MailAnalyticsAcceptedMessages acceptedMessages() {
        return acceptedMessages;
    }

    /** egress tasks created for the listener, including protocol-generated tasks */
    public long egressTasks() {
        return egressTasks;
    }

    /** unique succeeded tasks over unique terminal tasks */
    public MailAnalyticsRate deliverySuccessRate() {
        return deliverySuccessRate;
    }

    /** SMTP response class counts */
    public MailAnalyticsSMTPResponses smtpResponses() {
        return smtpResponses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsListenerOverviewMetrics that = (MailAnalyticsListenerOverviewMetrics) o;
        return Objects.equals(this.acceptedMessages, that.acceptedMessages)
                && this.egressTasks == that.egressTasks
                && Objects.equals(this.deliverySuccessRate, that.deliverySuccessRate)
                && Objects.equals(this.smtpResponses, that.smtpResponses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.acceptedMessages, this.egressTasks, this.deliverySuccessRate, this.smtpResponses);
    }

    @Override
    public String toString() {
        return "MailAnalyticsListenerOverviewMetrics[" + "acceptedMessages=" + this.acceptedMessages + ", " + "egressTasks=" + this.egressTasks + ", " + "deliverySuccessRate=" + this.deliverySuccessRate + ", " + "smtpResponses=" + this.smtpResponses + "]";
    }

    /**
     * Creates a new MailAnalyticsListenerOverviewMetrics.
     *
     * @param acceptedMessages accepted message counts split by channel
     * @param egressTasks egress tasks created for the listener, including protocol-generated tasks
     * @param deliverySuccessRate unique succeeded tasks over unique terminal tasks
     * @param smtpResponses SMTP response class counts
     */
    @JsonCreator
    public MailAnalyticsListenerOverviewMetrics(MailAnalyticsAcceptedMessages acceptedMessages, long egressTasks, MailAnalyticsRate deliverySuccessRate, MailAnalyticsSMTPResponses smtpResponses) {
        this.acceptedMessages = acceptedMessages;
        this.egressTasks = egressTasks;
        this.deliverySuccessRate = deliverySuccessRate;
        this.smtpResponses = smtpResponses;
    }
}
