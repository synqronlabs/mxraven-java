package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.Map;

/**
 * Effective tenant quota state and usage.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class TenantQuota {
    private final String tenantId;
    private final String tenantSlug;
    private final TenantQuotaGuardrailProfile guardrailProfile;
    private final EffectiveTenantQuotas effective;
    private final TenantQuotaUsage usage;
    private final Map<String, Boolean> overLimit;

    /** identifier of the tenant */
    public String tenantId() {
        return tenantId;
    }

    /** slug of the tenant */
    public String tenantSlug() {
        return tenantSlug;
    }

    /** guardrail profile applied to the tenant */
    public TenantQuotaGuardrailProfile guardrailProfile() {
        return guardrailProfile;
    }

    /** effective quota values */
    public EffectiveTenantQuotas effective() {
        return effective;
    }

    /** current resource usage */
    public TenantQuotaUsage usage() {
        return usage;
    }

    /** map of quota name to whether the tenant exceeds the limit */
    public Map<String, Boolean> overLimit() {
        return overLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TenantQuota that = (TenantQuota) o;
        return Objects.equals(this.tenantId, that.tenantId)
                && Objects.equals(this.tenantSlug, that.tenantSlug)
                && Objects.equals(this.guardrailProfile, that.guardrailProfile)
                && Objects.equals(this.effective, that.effective)
                && Objects.equals(this.usage, that.usage)
                && Objects.equals(this.overLimit, that.overLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.tenantId, this.tenantSlug, this.guardrailProfile, this.effective, this.usage, this.overLimit);
    }

    @Override
    public String toString() {
        return "TenantQuota[" + "tenantId=" + this.tenantId + ", " + "tenantSlug=" + this.tenantSlug + ", " + "guardrailProfile=" + this.guardrailProfile + ", " + "effective=" + this.effective + ", " + "usage=" + this.usage + ", " + "overLimit=" + this.overLimit + "]";
    }

    /**
     * Creates a new TenantQuota.
     *
     * @param tenantId identifier of the tenant
     * @param tenantSlug slug of the tenant
     * @param guardrailProfile guardrail profile applied to the tenant
     * @param effective effective quota values
     * @param usage current resource usage
     * @param overLimit map of quota name to whether the tenant exceeds the limit
     */
    @JsonCreator
    public TenantQuota(String tenantId, String tenantSlug, TenantQuotaGuardrailProfile guardrailProfile, EffectiveTenantQuotas effective, TenantQuotaUsage usage, Map<String, Boolean> overLimit) {
        this.tenantId = tenantId;
        this.tenantSlug = tenantSlug;
        this.guardrailProfile = guardrailProfile;
        this.effective = effective;
        this.usage = usage;
        this.overLimit = overLimit;
    }
}
