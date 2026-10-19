package com.mxraven.mail.notify;

/**
 * A notification body did not match the SDK's payload model.
 *
 * <p>This normally means the SDK is older than the server that produced the
 * event. The event is terminated rather than retried, because redelivering it
 * can never succeed. When an error handler is configured on the client, the
 * handler receives the error and consumption continues with the next event;
 * otherwise consumption stops and this exception is thrown.
 */
public class NotificationDecodeException extends NotificationException {
    private final String subject;

    /**
     * Creates a decode failure with a detail message.
     *
     * @param subject the notification subject the undecodable body was published to
     * @param detail  a description of the mismatch
     */
    public NotificationDecodeException(String subject, String detail) {
        super(message(subject, detail));
        this.subject = subject;
    }

    /**
     * Creates a decode failure caused by another failure.
     *
     * @param subject the notification subject the undecodable body was published to
     * @param cause   the underlying cause
     */
    public NotificationDecodeException(String subject, Throwable cause) {
        super(message(subject, cause == null ? null : cause.getMessage()), cause);
        this.subject = subject;
    }

    /**
     * The notification subject the undecodable body was published to.
     *
     * @return the subject
     */
    public String subject() {
        return subject;
    }

    private static String message(String subject, String detail) {
        String base = "notify: decode \"" + subject + "\"";
        return detail == null || detail.isEmpty() ? base : base + ": " + detail;
    }
}
