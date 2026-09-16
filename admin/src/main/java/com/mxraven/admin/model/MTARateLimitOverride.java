package com.mxraven.admin.model;

/**
 * Effective MTA rate-limit override state for a scope.
 *
 * <p>{@code scope} is one of {@code global}, {@code tenant}, or
 * {@code listener}; {@code effectiveSource} is one of {@code default},
 * {@code global}, {@code guardrail}, {@code tenant}, or {@code listener}.
 * {@code override} is present only when an explicit override exists at this
 * scope.
 *
 * @param scope           scope this override applies at
 * @param tenantId        tenant identifier for a tenant-scoped override
 * @param listenerId      listener identifier for a listener-scoped override
 * @param override        explicit override at this scope, if any
 * @param inherited       limits inherited from enclosing scopes
 * @param effective       effective limits after applying the override
 * @param effectiveSource source of the effective limits
 */
public record MTARateLimitOverride(
        MtaRateLimitScope scope,
        String tenantId,
        String listenerId,
        MTARateLimitPolicy override,
        MTARateLimitPolicy inherited,
        MTARateLimitPolicy effective,
        MtaRateLimitSource effectiveSource) {
}
