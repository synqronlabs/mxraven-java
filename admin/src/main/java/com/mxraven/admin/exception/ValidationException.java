package com.mxraven.admin.exception;

import com.mxraven.admin.Problem;

/**
 * A {@code 422 Unprocessable Content} response: the request is syntactically
 * valid but semantically rejected. Field-level issues are available from
 * {@link #errors()}.
 */
public final class ValidationException extends ApiException {
    ValidationException(String fallbackDetail, Problem problem) {
        super(422, fallbackDetail, problem);
    }
}
