package com.mxraven.mail.exception;

/**
 * Base exception for all mxRaven SDK errors.
 */
public class MxRavenException extends RuntimeException {
    public MxRavenException(String message) {
        super(message);
    }

    public MxRavenException(String message, Throwable cause) {
        super(message, cause);
    }
}
