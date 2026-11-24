package com.mxraven.admin.model;

/**
 * Wire representation backing the {@link WebhookEndpoint} entity.
 *
 * @param id unique endpoint identifier
 * @param tenantId identifier of the owning tenant
 * @param webhookRef stable reference for the endpoint
 * @param displayName human-readable endpoint name
 * @param targetUrl HTTPS URL that routing scripts call with mx.http
 * @param signingKid key identifier used to sign endpoint calls
 * @param hasSigningSecret whether a signing secret is stored
 * @param credentialHeader header carrying the stored credential, when one is set
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
        String credentialHeader,
        boolean isActive,
        String createdAt,
        String updatedAt) {
}
