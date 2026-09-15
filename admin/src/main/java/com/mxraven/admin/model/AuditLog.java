package com.mxraven.admin.model;

import java.util.Map;

/**
 * One tenant audit log entry.
 *
 * <p>{@code actorKind} is one of {@code human}, {@code machine}, or
 * {@code system}; {@code status} is {@code success} or {@code failure}.
 * {@code details} is action-specific free-form JSON.
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
