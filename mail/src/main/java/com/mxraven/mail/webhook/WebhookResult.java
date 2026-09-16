package com.mxraven.mail.webhook;

/**
 * The outcome of handling a webhook request, with the HTTP status a server
 * should return.
 *
 * <p>Return {@link #ACCEPTED} (or {@link #DUPLICATE}) to acknowledge a delivery.
 * The sender treats {@code 429} and {@code 5xx} as retryable and other non-2xx
 * responses as permanent failures.
 */
public enum WebhookResult {
    /** The delivery was verified and handed to the listener. Respond {@code 204}. */
    ACCEPTED(204),
    /** The delivery was already processed. Respond {@code 200} without dispatching. */
    DUPLICATE(200),
    /** The signature was missing or did not match. Respond {@code 401}. */
    INVALID_SIGNATURE(401),
    /** The request was missing headers, stale, oversized, or not decodable. Respond {@code 400}. */
    BAD_REQUEST(400);

    private final int status;

    WebhookResult(int status) {
        this.status = status;
    }

    /**
     * Returns the HTTP status code to return to the sender.
     *
     * @return the HTTP status code
     */
    public int status() {
        return status;
    }
}
