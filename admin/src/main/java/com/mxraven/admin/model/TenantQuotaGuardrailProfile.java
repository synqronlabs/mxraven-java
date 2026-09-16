package com.mxraven.admin.model;

/**
 * The guardrail profile summary embedded in a {@link TenantQuota}.
 *
 * @param id unique profile identifier
 * @param profileRef stable reference for the profile
 * @param displayName human-readable profile name
 * @param isActive whether the profile is active
 */
public record TenantQuotaGuardrailProfile(
        String id,
        String profileRef,
        String displayName,
        boolean isActive) {
}
