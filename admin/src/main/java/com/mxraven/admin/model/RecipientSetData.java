package com.mxraven.admin.model;

/** Wire representation backing the {@link RecipientSet} entity. */
public record RecipientSetData(
        String id,
        String tenantId,
        String setRef,
        String displayName,
        String description,
        String createdAt,
        String updatedAt) {
}
