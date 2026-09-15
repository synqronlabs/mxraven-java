package com.mxraven.admin;

import java.time.Duration;

/**
 * Client-wide control-plane rate-limit behaviour.
 *
 * <p>The v2 control plane signals limits with a {@code 429} response and a
 * {@code Retry-After} header (seconds or an HTTP date). When enabled,
 * {@link AdminClient} waits for that delay and transparently retries <em>GET</em>
 * and <em>DELETE</em> requests up to {@link #maxRetries()} times before surfacing a
 * {@link com.mxraven.admin.exception.RateLimitException}. Other mutations
 * (POST/PUT) are never retried automatically, so they cannot be applied twice or
 * amplify load.
 *
 * <pre>{@code
 * RateLimitConfig rateLimits = RateLimitConfig.builder()
 *         .maxRetries(5)
 *         .defaultBackoff(Duration.ofSeconds(2))
 *         .maxBackoff(Duration.ofMinutes(2))
 *         .build();
 * AdminClient admin = new AdminClient(baseUrl, token, rateLimits);
 * }</pre>
 */
public final class RateLimitConfig {
    private static final RateLimitConfig DEFAULTS = builder().build();

    private final boolean enabled;
    private final int maxRetries;
    private final Duration defaultBackoff;
    private final Duration maxBackoff;

    private RateLimitConfig(Builder builder) {
        this.enabled = builder.enabled;
        this.maxRetries = builder.maxRetries;
        this.defaultBackoff = builder.defaultBackoff;
        this.maxBackoff = builder.maxBackoff;
    }

    /** Retry on {@code 429} up to 3 times, honouring {@code Retry-After}. */
    public static RateLimitConfig defaults() {
        return DEFAULTS;
    }

    /** Surface {@code 429} immediately without retrying. */
    public static RateLimitConfig disabled() {
        return builder().enabled(false).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    /** Whether {@code 429} responses are retried. */
    public boolean enabled() {
        return enabled;
    }

    /** Maximum retries after the initial attempt. */
    public int maxRetries() {
        return maxRetries;
    }

    /** Delay used when a {@code 429} has no usable {@code Retry-After}. */
    public Duration defaultBackoff() {
        return defaultBackoff;
    }

    /** Upper bound applied to any server-provided {@code Retry-After}. */
    public Duration maxBackoff() {
        return maxBackoff;
    }

    public static final class Builder {
        private boolean enabled = true;
        private int maxRetries = 3;
        private Duration defaultBackoff = Duration.ofSeconds(1);
        private Duration maxBackoff = Duration.ofSeconds(60);

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder defaultBackoff(Duration defaultBackoff) {
            this.defaultBackoff = defaultBackoff;
            return this;
        }

        public Builder maxBackoff(Duration maxBackoff) {
            this.maxBackoff = maxBackoff;
            return this;
        }

        public RateLimitConfig build() {
            if (maxRetries < 0) {
                throw new IllegalArgumentException("maxRetries must not be negative");
            }
            if (defaultBackoff == null || defaultBackoff.isNegative()) {
                throw new IllegalArgumentException("defaultBackoff must not be negative");
            }
            if (maxBackoff == null || maxBackoff.isNegative()) {
                throw new IllegalArgumentException("maxBackoff must not be negative");
            }
            return new RateLimitConfig(this);
        }
    }
}
