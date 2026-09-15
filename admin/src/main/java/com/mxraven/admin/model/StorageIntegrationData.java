package com.mxraven.admin.model;

/** Wire representation backing the {@link StorageIntegration} entity. */
public record StorageIntegrationData(
        String id,
        String tenantId,
        String storageRef,
        String displayName,
        boolean isActive,
        boolean credentialsPresent) {
}
