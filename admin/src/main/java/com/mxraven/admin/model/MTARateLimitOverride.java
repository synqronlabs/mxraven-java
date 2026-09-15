package com.mxraven.admin.model;

/**
 * Effective MTA rate-limit override state for a scope.
 *
 * <p>{@code scope} is one of {@code global}, {@code tenant}, or
 * {@code listener}; {@code effectiveSource} is one of {@code default},
 * {@code global}, {@code guardrail}, {@code tenant}, or {@code listener}.
 * {@code override} is present only when an explicit override exists at this
 * scope.
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
