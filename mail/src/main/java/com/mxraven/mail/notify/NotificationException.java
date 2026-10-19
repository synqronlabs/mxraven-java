package com.mxraven.mail.notify;

import com.mxraven.mail.exception.MxRavenException;

/**
 * A client notification connection or consume operation failed.
 *
 * <p>Most connection failures are transient and are retried automatically;
 * this exception is only thrown when an operation cannot continue, or when the
 * client is used after it has been closed.
 */
public class NotificationException extends MxRavenException {
    /**
     * Creates an exception with the given message.
     *
     * @param message the detail message
     */
    public NotificationException(String message) {
        super(message);
    }

    /**
     * Creates an exception with the given message and cause.
     *
     * @param message the detail message
     * @param cause   the underlying cause
     */
    public NotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
