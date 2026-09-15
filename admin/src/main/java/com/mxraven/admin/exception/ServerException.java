package com.mxraven.admin.exception;

import com.mxraven.admin.Problem;

import java.time.Duration;

/**
 * A {@code 5xx} response: the control plane failed to process an otherwise valid
 * request. These are generally safe to retry for idempotent operations.
 */
public final class ServerException extends ApiException {
    ServerException(int status, String fallbackDetail, Problem problem, Duration retryAfter) {
        super(status, fallbackDetail, problem, retryAfter);
    }
}
