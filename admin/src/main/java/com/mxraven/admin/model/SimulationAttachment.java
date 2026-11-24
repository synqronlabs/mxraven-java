package com.mxraven.admin.model;

/**
 * One synthesized attachment's metadata in a simulation fixture. Its content is
 * never needed: only what {@code mail.body:attachments()} reports.
 *
 * @param filename the attachment filename
 * @param contentType the attachment content type
 * @param sizeBytes the attachment size in bytes
 * @param inline whether the attachment is inline
 */
public record SimulationAttachment(
        String filename,
        String contentType,
        Long sizeBytes,
        Boolean inline) {
}
