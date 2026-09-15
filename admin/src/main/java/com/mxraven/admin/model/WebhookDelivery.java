package com.mxraven.admin.model;

/**
 * Retained metadata for one webhook delivery attempt. Request content, response
 * bodies, and raw upstream errors are not exposed.
 *
 * <p>{@code deliveryKind} is {@code deliver_webhook} or {@code notify_webhook}.
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
