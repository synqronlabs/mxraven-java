package com.mxraven.mail.webhook;

import com.mxraven.mail.exception.MxRavenException;

/**
 * A webhook delivery could not be verified or decoded.
 */
public class WebhookException extends MxRavenException {
    public WebhookException(String message) {
        super(message);
    }

    public WebhookException(String message, Throwable cause) {
        super(message, cause);
    }
}
