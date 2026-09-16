package com.mxraven.admin.model;

/**
 * One non-secret tenant resource match from resource search.
 *
 * <p>{@code resourceType} is a {@code TenantSearchResourceType} value. Results
 * are ordered by exact, then prefix, then substring relevance.
 *
 * @param resourceType type of the matched resource
 * @param resourceId identifier of the matched resource
 * @param parentResourceId identifier of the parent resource, if any
 * @param label human-readable resource label
 * @param reference stable resource reference
 * @param description resource description
 */
public record TenantResourceSearchResult(
        TenantSearchResourceType resourceType,
        String resourceId,
        String parentResourceId,
        String label,
        String reference,
        String description) {
}
