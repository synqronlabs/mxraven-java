package com.mxraven.mail.feedback;

/**
 * The outcome of a successful learning request.
 *
 * @param status          the service status, normally {@code "learned"}
 * @param disposition     the training label that was applied
 * @param tenantId        the tenant that owns the matched message
 * @param listenerId      the listener that processed the matched message
 * @param matchedHashKind which stored hash matched the submitted bytes;
 *                        {@code rendered_eml_sha256} or {@code accepted_eml_sha256}
 */
public record LearningResult(
        String status,
        Disposition disposition,
        String tenantId,
        String listenerId,
        String matchedHashKind) {
}
