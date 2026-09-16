package com.mxraven.admin.model;

import java.util.Map;

/**
 * Effective tenant quota state and usage.
 *
 * @param tenantId identifier of the tenant
 * @param tenantSlug slug of the tenant
 * @param guardrailProfile guardrail profile applied to the tenant
 * @param effective effective quota values
 * @param usage current resource usage
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
