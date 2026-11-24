package com.mxraven.admin.model;

/**
 * A dedicated IP pool leased to a tenant.
 *
 * <p>{@code streamType} is one of {@code transactional} or {@code marketing}.
 *
 * @param id unique pool identifier
 * @param name human-readable pool name
 * @param streamType mail stream the pool serves
 * @param ref tenant-scoped lease reference routing scripts use under {@code deps.pool}
 */
public record TenantDedicatedIPPool(
        String id,
        String name,
        StreamType streamType,
        String ref) {
}
