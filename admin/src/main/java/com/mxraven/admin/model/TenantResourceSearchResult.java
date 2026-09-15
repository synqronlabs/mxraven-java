package com.mxraven.admin.model;

/**
 * One non-secret tenant resource match from resource search.
 *
 * <p>{@code resourceType} is a {@code TenantSearchResourceType} value. Results
 * are ordered by exact, then prefix, then substring relevance.
 */
public record TenantResourceSearchResult(
        TenantSearchResourceType resourceType,
        String resourceId,
        String parentResourceId,
        String label,
        String reference,
        String description) {
}
