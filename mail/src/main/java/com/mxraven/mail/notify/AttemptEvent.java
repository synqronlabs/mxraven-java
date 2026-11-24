package com.mxraven.mail.notify;

/**
 * One delivery attempt for an accepted message.
 *
 * <p>{@link #channel()} names the worker that produced the event. The
 * remaining fields describe that channel's protocol and are absent when they
 * do not apply. A routing script that stages several effects reports each one,
 * so a message can produce more than one attempt event.
 *
 * <p>Every attempt carries a {@link #correlationId()} that points at the
 * {@code taskId} of the accepted event for the message it stems from, which is
 * how an attempt joins back to its acceptance.
 *
 * @param channel the egress worker that produced the attempt
 * @param taskId the delivery task; for a submission listener it equals the accepted event's task id
 * @param tenantId the owning tenant
 * @param listenerId the source listener
 * @param messageRef the mxRaven message reference of the message this attempt belongs to
 * @param correlationId the task id of the accepted event for the message, when one was recorded
 * @param status the attempt outcome
 * @param attempt the delivery attempt number, starting at 1
 * @param acceptedAtUtc Unix time, in seconds, when mxRaven accepted the message
 * @param occurredAtUtc Unix time, in seconds, when the attempt was built
 * @param sourceIp the SMTP egress source address, when known
 * @param destinationDomain the recipient domain for an SMTP attempt
 * @param remoteHost the remote MTA hostname, when known
 * @param smtpCode the remote SMTP reply code
 * @param enhancedStatusCode the RFC 3463 enhanced status code
 * @param reason the bounded, machine-readable delivery classification
 * @param remoteResponse the remote reply text, truncated by the server
 * @param correlationTaskId links a generated DSN back to its original task
 * @param storageRef the storage integration identifier for an {@code s3} attempt
 * @param bucketName the destination bucket for an {@code s3} attempt
 * @param objectKey the stored object key for an {@code s3} attempt
 * @param endpointHost the storage endpoint host for an {@code s3} attempt
 * @param errorCode the storage provider error code, when one was returned
 * @param statusCode the storage HTTP response status for an {@code s3} attempt
 * @param message the failure description, truncated by the server
 * @param nextRetryAtUtc Unix time, in seconds, of the next attempt when the attempt will be retried
 */
public record AttemptEvent(
        AttemptChannel channel,
        String taskId,
        String tenantId,
        String listenerId,
        String messageRef,
        String correlationId,
        StatusOutcome status,
        long attempt,
        Long acceptedAtUtc,
        long occurredAtUtc,
        String sourceIp,
        String destinationDomain,
        String remoteHost,
        Integer smtpCode,
        String enhancedStatusCode,
        String reason,
        String remoteResponse,
        String correlationTaskId,
        String storageRef,
        String bucketName,
        String objectKey,
        String endpointHost,
        String errorCode,
        Integer statusCode,
        String message,
        Long nextRetryAtUtc) {
}
