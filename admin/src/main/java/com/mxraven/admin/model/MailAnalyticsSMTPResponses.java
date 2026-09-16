package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * SMTP response class counts from SMTP-channel lifecycle facts with a non-zero
 * SMTP status code.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsSMTPResponses {
    private final long successful;
    private final long temporaryFailure;
    private final long permanentFailure;

    /** count of 2xx SMTP responses */
    public long successful() {
        return successful;
    }

    /** count of 4xx SMTP responses */
    public long temporaryFailure() {
        return temporaryFailure;
    }

    /** count of 5xx SMTP responses */
    public long permanentFailure() {
        return permanentFailure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsSMTPResponses that = (MailAnalyticsSMTPResponses) o;
        return this.successful == that.successful
                && this.temporaryFailure == that.temporaryFailure
                && this.permanentFailure == that.permanentFailure;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.successful, this.temporaryFailure, this.permanentFailure);
    }

    @Override
    public String toString() {
        return "MailAnalyticsSMTPResponses[" + "successful=" + this.successful + ", " + "temporaryFailure=" + this.temporaryFailure + ", " + "permanentFailure=" + this.permanentFailure + "]";
    }

    /**
     * Creates a new MailAnalyticsSMTPResponses.
     *
     * @param successful count of 2xx SMTP responses
     * @param temporaryFailure count of 4xx SMTP responses
     * @param permanentFailure count of 5xx SMTP responses
     */
    @JsonCreator
    public MailAnalyticsSMTPResponses(long successful, long temporaryFailure, long permanentFailure) {
        this.successful = successful;
        this.temporaryFailure = temporaryFailure;
        this.permanentFailure = permanentFailure;
    }
}
