package com.mxraven.admin.exception;

import com.mxraven.admin.Problem;

/**
 * A {@code 404 Not Found} response: the addressed resource does not exist or is
 * not visible to the tenant.
 */
public final class NotFoundException extends ApiException {
    NotFoundException(String fallbackDetail, Problem problem) {
        super(404, fallbackDetail, problem);
    }
}
