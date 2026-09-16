/**
 * Exception hierarchy for the mxRaven admin SDK.
 *
 * <p>All SDK errors extend {@link MxRavenException}. Failures from the
 * control plane are represented by {@link ApiException}, with common HTTP
 * statuses surfaced as typed subclasses such as {@link NotFoundException} and
 * {@link ValidationException}.
 */
package com.mxraven.admin.exception;
