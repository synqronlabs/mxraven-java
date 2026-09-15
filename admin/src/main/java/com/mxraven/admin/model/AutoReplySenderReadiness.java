package com.mxraven.admin.model;

/**
 * Whether an auto-reply template's sender can currently deliver.
 *
 * <p>{@code status} is one of {@code ready}, {@code invalid_from_address},
 * {@code domain_not_found}, {@code sending_not_enabled},
 * {@code domain_not_verified}, or {@code dkim_not_verified}.
 */
public record AutoReplySenderReadiness(
        boolean ready,
        AutoReplySenderReadinessStatus status) {
}
