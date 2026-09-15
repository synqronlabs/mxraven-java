package com.mxraven.admin.model;

import java.util.Map;

/**
 * Effective tenant quota state and usage.
 *
 * @param overLimit map of quota name to whether the tenant exceeds the limit
 */
public record TenantQuota(
        String tenantId,
        String tenantSlug,
        TenantQuotaGuardrailProfile guardrailProfile,
        EffectiveTenantQuotas effective,
        TenantQuotaUsage usage,
        Map<String, Boolean> overLimit) {
}
