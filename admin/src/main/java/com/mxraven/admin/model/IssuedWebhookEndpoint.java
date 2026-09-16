package com.mxraven.admin.model;

/**
 * A webhook endpoint returned by creation or secret rotation, including the
 * one-time {@code signingSecret}.
 *
 * @param id unique webhook endpoint identifier
 * @param tenantId identifier of the owning tenant
 * @param webhookRef stable reference used when delivering events
 * @param displayName human-readable endpoint name
 * @param targetUrl URL webhooks are delivered to
 * @param signingKid identifier of the active signing key
 * @param hasSigningSecret whether a signing secret is configured
 * @param isActive whether the endpoint is active
 * @param createdAt creation timestamp
 * @param updatedAt timestamp of the last update
 * @param signingSecret one-time plaintext signing secret, returned only on creation or rotation
 */
public record IssuedWebhookEndpoint(
        String id,
        String tenantId,
        String webhookRef,
        String displayName,
        String targetUrl,
        String signingKid,
        boolean hasSigningSecret,
        boolean isActive,
        String createdAt,
        String updatedAt,
        String signingSecret) {
}
