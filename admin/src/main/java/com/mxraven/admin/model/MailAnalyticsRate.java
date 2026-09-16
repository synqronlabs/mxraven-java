package com.mxraven.admin.model;

/**
 * A bounded rate with its numerator and denominator.
 *
 * <p>{@code value} is null when the denominator is zero.
 *
 * @param numerator rate numerator
 * @param denominator rate denominator
 * @param value computed rate, or {@code null} when the denominator is zero
 */
public record MailAnalyticsRate(
        long numerator,
        long denominator,
        Double value) {
}
