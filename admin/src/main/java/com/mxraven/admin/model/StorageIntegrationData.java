package com.mxraven.admin.model;

/**
 * Wire representation backing the {@link StorageIntegration} entity.
 *
 * @param id unique integration identifier
 * @param tenantId identifier of the owning tenant
 * @param storageRef stable reference for the integration
 * @param displayName human-readable integration name
 * @param isActive whether the integration is active
 * @param credentialsPresent whether storage credentials are stored
 */
public record StorageIntegrationData(
        String id,
        String tenantId,
        String storageRef,
        String displayName,
        boolean isActive,
        boolean credentialsPresent) {
}
