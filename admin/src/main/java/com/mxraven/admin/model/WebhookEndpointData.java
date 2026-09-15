package com.mxraven.admin.model;

/** Wire representation backing the {@link WebhookEndpoint} entity. */
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
