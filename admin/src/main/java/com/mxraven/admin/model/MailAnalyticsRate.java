package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * A bounded rate with its numerator and denominator.
 *
 * <p>{@code value} is null when the denominator is zero.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsRate {
    private final long numerator;
    private final long denominator;
    private final Double value;

    /** rate numerator */
    public long numerator() {
        return numerator;
    }

    /** rate denominator */
    public long denominator() {
        return denominator;
    }

    /** computed rate, or {@code null} when the denominator is zero */
    public Double value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsRate that = (MailAnalyticsRate) o;
        return this.numerator == that.numerator
                && this.denominator == that.denominator
                && Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.numerator, this.denominator, this.value);
    }

    @Override
    public String toString() {
        return "MailAnalyticsRate[" + "numerator=" + this.numerator + ", " + "denominator=" + this.denominator + ", " + "value=" + this.value + "]";
    }

    /**
     * Creates a new MailAnalyticsRate.
     *
     * @param numerator rate numerator
     * @param denominator rate denominator
     * @param value computed rate, or {@code null} when the denominator is zero
     */
    @JsonCreator
    public MailAnalyticsRate(long numerator, long denominator, Double value) {
        this.numerator = numerator;
        this.denominator = denominator;
        this.value = value;
    }
}
