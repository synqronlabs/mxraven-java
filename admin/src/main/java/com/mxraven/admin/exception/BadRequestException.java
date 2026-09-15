package com.mxraven.admin.exception;

import com.mxraven.admin.Problem;

/**
 * A {@code 400 Bad Request} response: the request was malformed or violated a
 * syntactic constraint.
 */
public final class BadRequestException extends ApiException {
    BadRequestException(String fallbackDetail, Problem problem) {
        super(400, fallbackDetail, problem);
    }
}
