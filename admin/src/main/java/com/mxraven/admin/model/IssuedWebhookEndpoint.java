package com.mxraven.admin.model;

/**
 * A webhook endpoint returned by creation or secret rotation, including the
 * one-time {@code signingSecret}.
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
