package com.mxraven.admin.model;

/**
 * Wire representation backing the {@link WebhookEndpoint} entity.
 *
 * @param id unique endpoint identifier
 * @param tenantId identifier of the owning tenant
 * @param webhookRef stable reference for the endpoint
 * @param displayName human-readable endpoint name
 * @param targetUrl HTTPS URL that deliveries are sent to
 * @param signingKid key identifier used to sign deliveries
 * @param hasSigningSecret whether a signing secret is stored
 * @param isActive whether the endpoint is active
 * @param createdAt timestamp when the endpoint was created
 * @param updatedAt timestamp when the endpoint was last updated
 */
public record WebhookEndpointData(
        String id,
        String tenantId,
        String webhookRef,
        String displayName,
        String targetUrl,
        String signingKid,
        boolean hasSigningSecret,
        boolean isActive,
        String createdAt,
        String updatedAt) {
}
