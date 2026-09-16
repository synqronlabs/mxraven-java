package com.mxraven.mail;

/**
 * An RFC 3463 enhanced status code carried in an SMTP reply, for example
 * {@code 5.1.1} (class 5, subject 1, detail 1).
 *
 * <p>Servers advertise support with the {@code ENHANCEDSTATUSCODES} extension and
 * then prefix reply text with the code. Use {@link SmtpResponse#enhancedStatus()}
 * to extract it.
 *
 * @param statusClass the status class: {@code 2}, {@code 4}, or {@code 5}
 * @param subject     the subject sub-code
 * @param detail      the detail sub-code
 */
public record EnhancedStatus(int statusClass, int subject, int detail) {
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
}
