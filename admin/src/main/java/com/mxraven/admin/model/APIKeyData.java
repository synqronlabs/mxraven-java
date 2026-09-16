package com.mxraven.admin.model;

/**
 * Wire representation of a submission-listener API key without secret material.
 * Prefer the {@link APIKey} entity.
 *
 * @param id API key identifier
 * @param tenantId owning tenant identifier
 * @param listenerId listener the key is scoped to
 * @param username submission username associated with the key
 */
public record APIKeyData(
        String id,
        String tenantId,
        String listenerId,
        String username) {
}
