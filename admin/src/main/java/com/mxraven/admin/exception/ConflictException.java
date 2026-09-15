package com.mxraven.admin.exception;

import com.mxraven.admin.Problem;

/**
 * A {@code 409 Conflict} response: the request conflicts with the current state,
 * for example creating a resource that already exists.
 */
public final class ConflictException extends ApiException {
    ConflictException(String fallbackDetail, Problem problem) {
        super(409, fallbackDetail, problem);
    }
}
