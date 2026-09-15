package com.mxraven.admin.model;

/**
 * Wire representation of a submission-listener API key without secret material.
 * Prefer the {@link APIKey} entity.
 */
public record APIKeyData(
        String id,
        String tenantId,
        String listenerId,
        String username) {
}
