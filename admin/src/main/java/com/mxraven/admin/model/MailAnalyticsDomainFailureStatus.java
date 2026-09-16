package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Aggregated failure status for a recipient-domain lifecycle cohort.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsDomainFailureStatus {
    private final String channel;
    private final int smtpStatusCode;
    private final String enhancedStatusCode;
    private final long applicationStatusCode;
    private final long count;

    /** submission or MTA channel */
    public String channel() {
        return channel;
    }

    /** SMTP status code */
    public int smtpStatusCode() {
        return smtpStatusCode;
    }

    /** enhanced status code */
    public String enhancedStatusCode() {
        return enhancedStatusCode;
    }

    /** application status code */
    public long applicationStatusCode() {
        return applicationStatusCode;
    }

    /** number of failures with this status */
    public long count() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsDomainFailureStatus that = (MailAnalyticsDomainFailureStatus) o;
        return Objects.equals(this.channel, that.channel)
                && this.smtpStatusCode == that.smtpStatusCode
                && Objects.equals(this.enhancedStatusCode, that.enhancedStatusCode)
                && this.applicationStatusCode == that.applicationStatusCode
                && this.count == that.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.channel, this.smtpStatusCode, this.enhancedStatusCode, this.applicationStatusCode, this.count);
    }

    @Override
    public String toString() {
        return "MailAnalyticsDomainFailureStatus[" + "channel=" + this.channel + ", " + "smtpStatusCode=" + this.smtpStatusCode + ", " + "enhancedStatusCode=" + this.enhancedStatusCode + ", " + "applicationStatusCode=" + this.applicationStatusCode + ", " + "count=" + this.count + "]";
    }

    /**
     * Creates a new MailAnalyticsDomainFailureStatus.
     *
     * @param channel submission or MTA channel
     * @param smtpStatusCode SMTP status code
     * @param enhancedStatusCode enhanced status code
     * @param applicationStatusCode application status code
     * @param count number of failures with this status
     */
    @JsonCreator
    public MailAnalyticsDomainFailureStatus(String channel, int smtpStatusCode, String enhancedStatusCode, long applicationStatusCode, long count) {
        this.channel = channel;
        this.smtpStatusCode = smtpStatusCode;
        this.enhancedStatusCode = enhancedStatusCode;
        this.applicationStatusCode = applicationStatusCode;
        this.count = count;
    }
}
