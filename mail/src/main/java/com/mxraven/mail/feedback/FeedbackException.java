package com.mxraven.mail.feedback;

import com.mxraven.mail.exception.MxRavenException;

/**
 * A non-success response from the mxRaven feedback service.
 *
 * <p>Branch on {@link #statusCode()} rather than on the human-readable
 * {@link #detail()}. {@link #retryable()} reports whether the request may
 * succeed if retried later.
 */
public final class FeedbackException extends MxRavenException {
    private final int statusCode;
    private final String detail;

    public FeedbackException(int statusCode, String detail) {
        super(message(statusCode, detail));
        this.statusCode = statusCode;
        this.detail = detail;
    }

    /** The HTTP response status. */
    public int statusCode() {
        return statusCode;
    }

    /** The service's error message, or {@code null} when none was returned. */
    public String detail() {
        return detail;
    }

    /**
     * Whether the request may succeed if retried later. Rate limits (429) and
     * server-side failures (5xx) are retryable.
     */
    public boolean retryable() {
        return statusCode == 429 || statusCode >= 500;
    }

    private static String message(int statusCode, String detail) {
        StringBuilder builder = new StringBuilder("HTTP ").append(statusCode);
        if (detail != null && !detail.isBlank()) {
            builder.append(": ").append(detail);
        }
        return builder.toString();
    }
}
