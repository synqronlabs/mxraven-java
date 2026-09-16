package com.mxraven.mail;

import java.util.Objects;

/**
 * An RFC 3463 enhanced status code carried in an SMTP reply, for example
 * {@code 5.1.1} (class 5, subject 1, detail 1).
 *
 * <p>Servers advertise support with the {@code ENHANCEDSTATUSCODES} extension and
 * then prefix reply text with the code. Use {@link SmtpResponse#enhancedStatus()}
 * to extract it.
 */
public final class EnhancedStatus {
    private final int statusClass;
    private final int subject;
    private final int detail;

    /**
     * Creates an enhanced status.
     *
     * @param statusClass the status class: {@code 2}, {@code 4}, or {@code 5}
     * @param subject     the subject sub-code
     * @param detail      the detail sub-code
     */
    public EnhancedStatus(int statusClass, int subject, int detail) {
        this.statusClass = statusClass;
        this.subject = subject;
        this.detail = detail;
    }

    /** the status class: {@code 2}, {@code 4}, or {@code 5} */
    public int statusClass() {
        return statusClass;
    }

    /** the subject sub-code */
    public int subject() {
        return subject;
    }

    /** the detail sub-code */
    public int detail() {
        return detail;
    }

    /**
     * Renders the code in its canonical dotted form.
     *
     * @return the code, for example {@code "5.1.1"}
     */
    public String code() {
        return statusClass + "." + subject + "." + detail;
    }

    /**
     * Whether the code reports success (class {@code 2}).
     *
     * @return {@code true} for a 2.x.x code
     */
    public boolean isSuccess() {
        return statusClass == 2;
    }

    /**
     * Whether the code reports a permanent or transient failure (class {@code 4}
     * or {@code 5}).
     *
     * @return {@code true} for a 4.x.x or 5.x.x code
     */
    public boolean isError() {
        return statusClass == 4 || statusClass == 5;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EnhancedStatus that = (EnhancedStatus) o;
        return this.statusClass == that.statusClass
                && this.subject == that.subject
                && this.detail == that.detail;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.statusClass, this.subject, this.detail);
    }

    @Override
    public String toString() {
        return "EnhancedStatus[" + "statusClass=" + this.statusClass + ", subject=" + this.subject
                + ", detail=" + this.detail + "]";
    }
}
