package com.mxraven.admin.model;

/**
 * An unbounded ratio with its numerator and denominator.
 *
 * <p>{@code value} is null when the denominator is zero.
 */
public record MailAnalyticsRatio(
        long numerator,
        long denominator,
        Double value) {
}
