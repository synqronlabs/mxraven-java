package com.mxraven.admin.model;

/** Wire representation backing the {@link TenantSuppression} entity. */
public record TenantSuppressionData(
        String tenantId,
        String emailAddress,
        SuppressionReason reason) {
}
