package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Terminal task outcomes counted by unique task ID after replacement
 * finalization.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsTerminalOutcomes {
    private final long succeeded;
    private final long failed;
    private final long expired;
    private final long suppressed;

    /** tasks that succeeded */
    public long succeeded() {
        return succeeded;
    }

    /** tasks that failed */
    public long failed() {
        return failed;
    }

    /** tasks that expired */
    public long expired() {
        return expired;
    }

    /** tasks that were suppressed */
    public long suppressed() {
        return suppressed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsTerminalOutcomes that = (MailAnalyticsTerminalOutcomes) o;
        return this.succeeded == that.succeeded
                && this.failed == that.failed
                && this.expired == that.expired
                && this.suppressed == that.suppressed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.succeeded, this.failed, this.expired, this.suppressed);
    }

    @Override
    public String toString() {
        return "MailAnalyticsTerminalOutcomes[" + "succeeded=" + this.succeeded + ", " + "failed=" + this.failed + ", " + "expired=" + this.expired + ", " + "suppressed=" + this.suppressed + "]";
    }

    /**
     * Creates a new MailAnalyticsTerminalOutcomes.
     *
     * @param succeeded tasks that succeeded
     * @param failed tasks that failed
     * @param expired tasks that expired
     * @param suppressed tasks that were suppressed
     */
    @JsonCreator
    public MailAnalyticsTerminalOutcomes(long succeeded, long failed, long expired, long suppressed) {
        this.succeeded = succeeded;
        this.failed = failed;
        this.expired = expired;
        this.suppressed = suppressed;
    }
}
