package com.mxraven.mail.webhook;

import com.mxraven.mail.exception.MxRavenException;

/**
 * A webhook delivery could not be verified or decoded.
 */
public class WebhookException extends MxRavenException {
    /**
     * Creates an exception with the given message.
     *
     * @param message the detail message
     */
    public WebhookException(String message) {
        super(message);
    }

    /**
     * Creates an exception with the given message and cause.
     *
     * @param message the detail message
     * @param cause   the underlying cause
     */
    public WebhookException(String message, Throwable cause) {
        super(message, cause);
    }
}
