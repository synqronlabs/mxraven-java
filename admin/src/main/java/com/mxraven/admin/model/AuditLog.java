package com.mxraven.admin.model;

import java.util.Map;

/**
 * One tenant audit log entry.
 *
 * <p>{@code actorKind} is one of {@code human}, {@code machine}, or
 * {@code system}; {@code status} is {@code success} or {@code failure}.
 * {@code details} is action-specific free-form JSON.
 *
 * @param id unique audit-log identifier
 * @param tenantId owning tenant identifier
 * @param actorId identifier of the acting user, machine, or system
 * @param actorKind kind of actor that produced the entry
 * @param action action that was performed
 * @param resourceType type of the affected resource
 * @param resourceId identifier of the affected resource
 * @param status outcome of the action
 * @param details action-specific free-form JSON
 * @param createdAt creation timestamp
 */
public record AuditLog(
        String id,
        String tenantId,
        String actorId,
        AuditActorKind actorKind,
        String action,
        String resourceType,
        String resourceId,
        AuditStatus status,
        Map<String, Object> details,
        String createdAt) {
}
