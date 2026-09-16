package com.mxraven.admin.model;

/**
 * An unbounded ratio with its numerator and denominator.
 *
 * <p>{@code value} is null when the denominator is zero.
 *
 * @param numerator ratio numerator
 * @param denominator ratio denominator
 * @param value computed ratio, or {@code null} when the denominator is zero
 */
public record MailAnalyticsRatio(
        long numerator,
        long denominator,
        Double value) {
}
