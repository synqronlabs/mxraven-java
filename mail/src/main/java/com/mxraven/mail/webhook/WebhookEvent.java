package com.mxraven.mail.webhook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * A decoded webhook delivery. One of {@link InboundEmail}, {@link DeliveryStatus},
 * or {@link StorageStatus}, matching {@link #type()}.
 *
 * <p>Branch with pattern matching:
 *
 * <pre>{@code
 * WebhookEvent event = verifier.verifyAndDecode(method, url, headers, body);
 * if (event instanceof InboundEmail email) {
 *     // DELIVER_WEBHOOK: a full inbound message.
 * } else if (event instanceof DeliveryStatus status) {
 *     // NOTIFY_WEBHOOK: an SMTP delivery status.
 * } else if (event instanceof StorageStatus storage) {
 *     // NOTIFY_WEBHOOK: an object-storage delivery status.
 * }
 * }</pre>
 */
public sealed interface WebhookEvent permits InboundEmail, DeliveryStatus, StorageStatus {

    /** Identifies the payload shape. */
    WebhookEventType type();

    /**
     * The delivery task ID. Stable across retries and shared by status
     * callbacks for the same task; use it to deduplicate at-least-once
     * deliveries.
     */
    String taskId();

    /**
     * Decodes a webhook payload body.
     *
     * <p>It dispatches on {@code event_type}. SMTP delivery statuses carry no
     * event type, so a body with a {@code status} field and no recognized event
     * type is decoded as a {@link DeliveryStatus}.
     *
     * <p>Decoding does not verify the request signature. Call
     * {@link WebhookVerifier#verifyAndDecode} first when the payload came from
     * the network.
     *
     * @throws WebhookException when the body is not a recognized payload
     */
    static WebhookEvent decode(byte[] body) {
        if (body == null) {
            throw new WebhookException("decode webhook payload: body is null");
        }
        JsonNode payload;
        try {
            payload = WebhookJson.MAPPER.readTree(body);
        } catch (IOException e) {
            throw new WebhookException("decode webhook payload: " + e.getMessage(), e);
        }
        if (payload == null || !payload.isObject()) {
            throw new WebhookException("decode webhook payload: expected a JSON object");
        }

        String eventType = payload.path("event_type").asText("");
        try {
            return switch (eventType) {
                case "inbound_email" -> WebhookJson.MAPPER.treeToValue(payload, InboundEmail.class);
                case "s3_egress_status" -> WebhookJson.MAPPER.treeToValue(payload, StorageStatus.class);
                default -> {
                    if (payload.hasNonNull("status")) {
                        yield WebhookJson.MAPPER.treeToValue(payload, DeliveryStatus.class);
                    }
                    throw new WebhookException(
                            "decode webhook payload: unrecognized event_type \"" + eventType + "\"");
                }
            };
        } catch (JsonProcessingException e) {
            throw new WebhookException("decode webhook payload: " + e.getOriginalMessage(), e);
        }
    }
}
