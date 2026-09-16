package com.mxraven.admin.model;

/**
 * Wire representation backing the {@link RecipientSet} entity.
 *
 * @param id          identifier of the recipient set
 * @param tenantId    identifier of the owning tenant
 * @param setRef      stable reference used to address the set
 * @param displayName human-readable name
 * @param description optional description
 * @param createdAt   creation timestamp
 * @param updatedAt   last update timestamp
 */
public record RecipientSetData(
        String id,
        String tenantId,
        String setRef,
        String displayName,
        String description,
        String createdAt,
        String updatedAt) {
}
