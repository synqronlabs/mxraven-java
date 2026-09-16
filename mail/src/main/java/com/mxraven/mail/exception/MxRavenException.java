package com.mxraven.mail.exception;

/**
 * Base exception for all mxRaven SDK errors.
 */
public class MxRavenException extends RuntimeException {
    /**
     * Creates an exception with the given detail message.
     *
     * @param message the detail message
     */
    public MxRavenException(String message) {
        super(message);
    }

    /**
     * Creates an exception with the given detail message and cause.
     *
     * @param message the detail message
     * @param cause the underlying cause
     */
    public MxRavenException(String message, Throwable cause) {
        super(message, cause);
    }
}
