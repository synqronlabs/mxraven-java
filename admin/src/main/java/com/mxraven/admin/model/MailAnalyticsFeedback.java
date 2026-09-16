package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Feedback fact counts derived from SMTP feedback, excluding forwarding audit
 * events.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsFeedback {
    private final long dsn;
    private final long arf;
    private final long oneClickUnsubscribe;
    private final long tenantTraining;

    /** delivery status notification feedback count */
    public long dsn() {
        return dsn;
    }

    /** abuse reporting format feedback count */
    public long arf() {
        return arf;
    }

    /** one-click unsubscribe feedback count */
    public long oneClickUnsubscribe() {
        return oneClickUnsubscribe;
    }

    /** tenant-supplied training feedback count */
    public long tenantTraining() {
        return tenantTraining;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsFeedback that = (MailAnalyticsFeedback) o;
        return this.dsn == that.dsn
                && this.arf == that.arf
                && this.oneClickUnsubscribe == that.oneClickUnsubscribe
                && this.tenantTraining == that.tenantTraining;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.dsn, this.arf, this.oneClickUnsubscribe, this.tenantTraining);
    }

    @Override
    public String toString() {
        return "MailAnalyticsFeedback[" + "dsn=" + this.dsn + ", " + "arf=" + this.arf + ", " + "oneClickUnsubscribe=" + this.oneClickUnsubscribe + ", " + "tenantTraining=" + this.tenantTraining + "]";
    }

    /**
     * Creates a new MailAnalyticsFeedback.
     *
     * @param dsn delivery status notification feedback count
     * @param arf abuse reporting format feedback count
     * @param oneClickUnsubscribe one-click unsubscribe feedback count
     * @param tenantTraining tenant-supplied training feedback count
     */
    @JsonCreator
    public MailAnalyticsFeedback(long dsn, long arf, long oneClickUnsubscribe, long tenantTraining) {
        this.dsn = dsn;
        this.arf = arf;
        this.oneClickUnsubscribe = oneClickUnsubscribe;
        this.tenantTraining = tenantTraining;
    }
}
