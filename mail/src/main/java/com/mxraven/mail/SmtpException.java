package com.mxraven.mail;

import com.mxraven.mail.exception.MxRavenException;

/**
 * Thrown when an SMTP conversation fails or a required capability is missing.
 */
public class SmtpException extends MxRavenException {
    /**
     * Creates an exception with the given detail message.
     *
     * @param message the detail message
     */
    public SmtpException(String message) {
        super(message);
    }

    /**
     * Creates an exception with the given detail message and cause.
     *
     * @param message the detail message
     * @param cause the underlying cause
     */
    public SmtpException(String message, Throwable cause) {
        super(message, cause);
    }
}
