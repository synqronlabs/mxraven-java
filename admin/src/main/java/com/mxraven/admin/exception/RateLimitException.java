package com.mxraven.admin.exception;

import com.mxraven.admin.Problem;

import java.time.Duration;

/**
 * A {@code 429 Too Many Requests} response: the account or tenant exceeded a
 * control-plane rate limit. The client retries <em>GET</em> and <em>DELETE</em>
 * responses transparently up to {@link com.mxraven.admin.RateLimitConfig#maxRetries()}
 * before throwing; other mutations always surface immediately. When thrown,
 * {@link #retryAfter()} carries the server-requested delay.
 */
public final class RateLimitException extends ApiException {
    RateLimitException(String fallbackDetail, Problem problem, Duration retryAfter) {
        super(429, fallbackDetail, problem, retryAfter);
    }
}
