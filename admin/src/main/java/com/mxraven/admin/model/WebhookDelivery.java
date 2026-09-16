package com.mxraven.admin.model;

/**
 * Retained metadata for one webhook delivery attempt. Request content, response
 * bodies, and raw upstream errors are not exposed.
 *
 * <p>{@code deliveryKind} is {@code deliver_webhook} or {@code notify_webhook}.
 *
 * @param id unique delivery identifier
 * @param taskId identifier of the delivery task
 * @param listenerId identifier of the listener that produced the delivery
 * @param deliveryKind kind of webhook delivery
 * @param outcome delivery outcome
 * @param attempt one-based attempt number
 * @param statusCode HTTP response status code, if any
 * @param targetUrlHost host of the delivery target
 * @param signingKid key identifier used to sign the delivery
 * @param occurredAt timestamp when the attempt occurred
 */
public record WebhookDelivery(
        String id,
        String taskId,
        String listenerId,
        WebhookDeliveryKind deliveryKind,
        String outcome,
        long attempt,
        Integer statusCode,
        String targetUrlHost,
        String signingKid,
        String occurredAt) {
}
