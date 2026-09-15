package com.mxraven.admin.exception;

import com.mxraven.admin.Problem;

/**
 * A {@code 401 Unauthorized} response: the bearer token is missing, invalid, or
 * expired.
 */
public final class AuthenticationException extends ApiException {
    AuthenticationException(String fallbackDetail, Problem problem) {
        super(401, fallbackDetail, problem);
    }
}
