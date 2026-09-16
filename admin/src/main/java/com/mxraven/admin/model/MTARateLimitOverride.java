package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Effective MTA rate-limit override state for a scope.
 *
 * <p>{@code scope} is one of {@code global}, {@code tenant}, or
 * {@code listener}; {@code effectiveSource} is one of {@code default},
 * {@code global}, {@code guardrail}, {@code tenant}, or {@code listener}.
 * {@code override} is present only when an explicit override exists at this
 * scope.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MTARateLimitOverride {
    private final MtaRateLimitScope scope;
    private final String tenantId;
    private final String listenerId;
    private final MTARateLimitPolicy override;
    private final MTARateLimitPolicy inherited;
    private final MTARateLimitPolicy effective;
    private final MtaRateLimitSource effectiveSource;

    /** scope this override applies at */
    public MtaRateLimitScope scope() {
        return scope;
    }

    /** tenant identifier for a tenant-scoped override */
    public String tenantId() {
        return tenantId;
    }

    /** listener identifier for a listener-scoped override */
    public String listenerId() {
        return listenerId;
    }

    /** explicit override at this scope, if any */
    public MTARateLimitPolicy override() {
        return override;
    }

    /** limits inherited from enclosing scopes */
    public MTARateLimitPolicy inherited() {
        return inherited;
    }

    /** effective limits after applying the override */
    public MTARateLimitPolicy effective() {
        return effective;
    }

    /** source of the effective limits */
    public MtaRateLimitSource effectiveSource() {
        return effectiveSource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MTARateLimitOverride that = (MTARateLimitOverride) o;
        return Objects.equals(this.scope, that.scope)
                && Objects.equals(this.tenantId, that.tenantId)
                && Objects.equals(this.listenerId, that.listenerId)
                && Objects.equals(this.override, that.override)
                && Objects.equals(this.inherited, that.inherited)
                && Objects.equals(this.effective, that.effective)
                && Objects.equals(this.effectiveSource, that.effectiveSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.scope, this.tenantId, this.listenerId, this.override, this.inherited, this.effective, this.effectiveSource);
    }

    @Override
    public String toString() {
        return "MTARateLimitOverride[" + "scope=" + this.scope + ", " + "tenantId=" + this.tenantId + ", " + "listenerId=" + this.listenerId + ", " + "override=" + this.override + ", " + "inherited=" + this.inherited + ", " + "effective=" + this.effective + ", " + "effectiveSource=" + this.effectiveSource + "]";
    }

    /**
     * Creates a new MTARateLimitOverride.
     *
     * @param scope scope this override applies at
     * @param tenantId tenant identifier for a tenant-scoped override
     * @param listenerId listener identifier for a listener-scoped override
     * @param override explicit override at this scope, if any
     * @param inherited limits inherited from enclosing scopes
     * @param effective effective limits after applying the override
     * @param effectiveSource source of the effective limits
     */
    @JsonCreator
    public MTARateLimitOverride(MtaRateLimitScope scope, String tenantId, String listenerId, MTARateLimitPolicy override, MTARateLimitPolicy inherited, MTARateLimitPolicy effective, MtaRateLimitSource effectiveSource) {
        this.scope = scope;
        this.tenantId = tenantId;
        this.listenerId = listenerId;
        this.override = override;
        this.inherited = inherited;
        this.effective = effective;
        this.effectiveSource = effectiveSource;
    }
}
