package com.mxraven.admin.exception;

/**
 * Base exception for all mxRaven SDK errors.
 */
public class MxRavenException extends RuntimeException {
    /**
     * Construct an exception with a message.
     *
     * @param message detail message
     */
    public MxRavenException(String message) {
        super(message);
    }

    /**
     * Construct an exception with a message and cause.
     *
     * @param message detail message
     * @param cause underlying cause
     */
    public MxRavenException(String message, Throwable cause) {
        super(message, cause);
    }
}
