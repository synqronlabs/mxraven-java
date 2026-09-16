package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Accepted message counts split by submission and MTA channel.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsAcceptedMessages {
    private final long total;
    private final long submission;
    private final long mta;

    /** total accepted messages */
    public long total() {
        return total;
    }

    /** messages accepted through submission listeners */
    public long submission() {
        return submission;
    }

    /** messages accepted through MTA listeners */
    public long mta() {
        return mta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsAcceptedMessages that = (MailAnalyticsAcceptedMessages) o;
        return this.total == that.total
                && this.submission == that.submission
                && this.mta == that.mta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.total, this.submission, this.mta);
    }

    @Override
    public String toString() {
        return "MailAnalyticsAcceptedMessages[" + "total=" + this.total + ", " + "submission=" + this.submission + ", " + "mta=" + this.mta + "]";
    }

    /**
     * Creates a new MailAnalyticsAcceptedMessages.
     *
     * @param total total accepted messages
     * @param submission messages accepted through submission listeners
     * @param mta messages accepted through MTA listeners
     */
    @JsonCreator
    public MailAnalyticsAcceptedMessages(long total, long submission, long mta) {
        this.total = total;
        this.submission = submission;
        this.mta = mta;
    }
}
