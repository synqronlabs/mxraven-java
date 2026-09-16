package com.mxraven.mail.webhook;

/**
 * Receives a verified webhook event.
 *
 * <p>This is the callback a {@link WebhookHandler} or {@link WebhookServer}
 * invokes after the signature has been verified. It is intentionally
 * framework-agnostic: use pattern matching to reach the payload you care about:
 *
 * <pre>{@code
 * WebhookListener listener = event -> {
 *     if (event instanceof InboundEmail email) {
 *         app.processInbound(email);
 *     } else if (event instanceof DeliveryStatus status) {
 *         app.recordStatus(status);
 *     }
 * };
 * }</pre>
 *
 * <p>A runtime exception thrown here propagates to the caller (or becomes a
 * {@code 500} when served by {@link WebhookServer}), so the sender retries.
 * Implementations that perform I/O should handle checked exceptions locally.
 *
 * <p>When you handle specific event types, use {@link WebhookCallbacks} to get
 * typed {@code onInboundEmail} / {@code onDeliveryStatus} / {@code onStorageStatus}
 * callbacks instead of an {@code instanceof} chain.
 */
@FunctionalInterface
public interface WebhookListener {
    /**
     * Handles one verified event.
     *
     * @param event the verified event
     */
    void onEvent(WebhookEvent event);
}
