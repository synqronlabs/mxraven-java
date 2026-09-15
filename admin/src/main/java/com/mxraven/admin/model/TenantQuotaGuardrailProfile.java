package com.mxraven.admin.model;

/**
 * The guardrail profile summary embedded in a {@link TenantQuota}.
 */
public record TenantQuotaGuardrailProfile(
        String id,
        String profileRef,
        String displayName,
        boolean isActive) {
}
