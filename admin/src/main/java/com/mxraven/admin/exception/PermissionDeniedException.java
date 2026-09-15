package com.mxraven.admin.exception;

import com.mxraven.admin.Problem;

/**
 * A {@code 403 Forbidden} response: the token is valid but lacks the scope or
 * tenant access required for the operation.
 */
public final class PermissionDeniedException extends ApiException {
    PermissionDeniedException(String fallbackDetail, Problem problem) {
        super(403, fallbackDetail, problem);
    }
}
