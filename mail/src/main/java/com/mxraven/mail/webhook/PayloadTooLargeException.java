package com.mxraven.mail.webhook;

/**
 * A signed endpoint call exceeded the verifier's configured body limit.
 *
 * <p>Return {@code 413} to the sender when this is thrown. Configure the limit
 * with {@link WebhookVerifier.Builder#maxBodyBytes(long)}; the default is
 * 1 MiB.
 */
public final class PayloadTooLargeException extends WebhookException {
    /**
     * Creates an exception for a body over the limit.
     *
     * @param message the detail message
     */
    public PayloadTooLargeException(String message) {
        super(message);
    }
}
