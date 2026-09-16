package com.mxraven.mail.webhook;

/**
 * Typed {@link WebhookListener} for when you handle specific event types.
 * Override only the callbacks you need; the others are ignored.
 *
 * <pre>
 * WebhookListener listener = new WebhookCallbacks() {
 *     &#64;Override
 *     public void onInboundEmail(InboundEmail email) {
 *         app.processInbound(email);
 *     }
 *
 *     &#64;Override
 *     public void onDeliveryStatus(DeliveryStatus status) {
 *         app.recordStatus(status);
 *     }
 * };
 * </pre>
 *
 * <p>Prefer the functional {@link WebhookListener} when a single lambda is
 * enough:
 *
 * <pre>
 * WebhookListener listener = event -&gt; app.handle(event);
 * </pre>
 *
 * <p>Because this extends {@link WebhookListener}, a {@code WebhookCallbacks}
 * can be passed anywhere a listener is accepted.
 */
public interface WebhookCallbacks extends WebhookListener {

    /** Dispatches to the typed callback for the event. */
    @Override
    default void onEvent(WebhookEvent event) {
        if (event instanceof InboundEmail) {
            onInboundEmail((InboundEmail) event);
        } else if (event instanceof DeliveryStatus) {
            onDeliveryStatus((DeliveryStatus) event);
        } else if (event instanceof StorageStatus) {
            onStorageStatus((StorageStatus) event);
        }
    }

    /**
     * Handles a {@code DELIVER_WEBHOOK} inbound message. Ignored by default.
     *
     * @param email the inbound message
     */
    default void onInboundEmail(InboundEmail email) {
    }

    /**
     * Handles an SMTP {@code NOTIFY_WEBHOOK} delivery status. Ignored by default.
     *
     * @param status the delivery status
     */
    default void onDeliveryStatus(DeliveryStatus status) {
    }

    /**
     * Handles an object-storage {@code NOTIFY_WEBHOOK} status. Ignored by default.
     *
     * @param storage the storage status
     */
    default void onStorageStatus(StorageStatus storage) {
    }
}
