package com.mxraven.admin;

import java.util.List;

/**
 * RFC 9457 Problem Details as returned by the v2 control plane with the
 * {@code application/problem+json} media type.
 *
 * @param type     stable URI identifying the problem category
 * @param title    short human-readable summary of the problem category
 * @param status   HTTP status code returned for this occurrence
 * @param code     stable machine-readable mxRaven error code; branch on this
 * @param detail   safe human-readable detail for this occurrence
 * @param instance URI reference identifying this occurrence, when available
 * @param traceId  identifier suitable for support correlation
 * @param errors   optional actionable validation issues
 */
public record Problem(
        String type,
        String title,
        Integer status,
        String code,
        String detail,
        String instance,
        String traceId,
        List<ProblemError> errors) {
}
