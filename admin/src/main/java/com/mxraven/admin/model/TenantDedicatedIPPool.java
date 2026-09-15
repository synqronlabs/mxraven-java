package com.mxraven.admin.model;

/**
 * A dedicated IP pool leased to a tenant.
 *
 * <p>{@code streamType} is one of {@code transactional} or {@code marketing}.
 */
public record TenantDedicatedIPPool(
        String id,
        String name,
        StreamType streamType) {
}
