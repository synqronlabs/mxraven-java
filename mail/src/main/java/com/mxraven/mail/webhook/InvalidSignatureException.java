package com.mxraven.mail.webhook;

/**
 * The HMAC signature of a webhook request did not match the configured secret.
 *
 * <p>Return a {@code 401} to the sender when this is thrown; do not process the
 * payload. The message includes the method, host, and request target the
 * signature was computed over, which helps diagnose proxy/host mismatches.
 */
public final class InvalidSignatureException extends WebhookException {
    /** Creates an exception with the default message. */
    public InvalidSignatureException() {
        this(null);
    }

    /**
     * Creates an exception that includes {@code detail} in the message.
     *
     * @param detail additional detail to append, or {@code null} to use the default message
     */
    public InvalidSignatureException(String detail) {
        super(detail == null || detail.isBlank()
                ? "webhook: invalid signature"
                : "webhook: invalid signature (" + detail + ")");
    }
}
