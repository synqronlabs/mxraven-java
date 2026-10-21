package com.mxraven.mail.notify;

import com.mxraven.mail.webhook.StatusOutcome;

/**
 * One delivery attempt for an accepted message.
 *
 * <p>{@link #channel()} names the worker that produced the event. The remaining
 * fields describe that channel's protocol and are absent when they do not
 * apply.
 *
 * <p>A rule that chains side-effect actions reports each action it triggers, so
 * a message can produce more than one attempt event. Every attempt carries a
 * {@link #correlationId()} that points at the {@code taskId} of the accepted
 * event for the message it stems from, which is how an attempt joins back to
 * its acceptance.
 *
 * @param eventType          the producer's event-type marker, when one is set; SMTP attempts omit it
 * @param channel            the egress worker that produced the attempt
 * @param taskId             the delivery task; for a submission listener this equals the accepted event's task id
 * @param tenantId           the owning tenant
 * @param listenerId         the source listener
 * @param messageRef         the mxRaven message reference of the message this attempt belongs to; it
 *                           joins attempts and acceptance events for the same message, including
 *                           every recipient a single send fanned out to
 * @param correlationId      the task id of the accepted event for the message, when one was recorded
 * @param status             the attempt outcome
 * @param attempt            the delivery attempt number, starting at 1
 * @param acceptedAtUtc      Unix time, in seconds, when mxRaven accepted the message
 * @param occurredAtUtc      Unix time, in seconds, when the attempt was built
 * @param sourceIp           the SMTP egress source address, when known
 * @param destinationDomain  the recipient domain for an SMTP attempt
 * @param remoteHost         the remote MTA hostname, when known
 * @param smtpCode           the remote SMTP reply code
 * @param enhancedStatusCode the RFC 3463 enhanced status code
 * @param reason             the bounded, machine-readable delivery classification
 * @param remoteResponse     the remote reply text, truncated by the server
 * @param correlationTaskId  links a generated DSN back to its original task
 * @param storageRef         the storage integration identifier for an {@code s3} attempt
 * @param bucketName         the destination bucket for an {@code s3} attempt
 * @param objectKey          the stored object key for an {@code s3} attempt
 * @param endpointHost       the storage endpoint host for an {@code s3} attempt
 * @param errorCode          the storage provider error code, when one was returned
 * @param statusCode         the storage or webhook HTTP response status
 * @param message            the failure description, truncated by the server
 * @param nextRetryAtUtc     Unix time, in seconds, of the next attempt when the attempt will be retried
 * @param retryAfterMs       the webhook retry-after hint, in milliseconds
 * @param targetUrlHost      the webhook target host
 * @param webhookRef         the webhook endpoint identifier
 * @param signingKeyKid      the key ID used to sign the webhook attempt
 * @param rawTokenTtlSeconds the raw-message token lifetime the webhook attempt used, in seconds
 */
public record AttemptEvent(
        String eventType,
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
        Long nextRetryAtUtc,
        Long retryAfterMs,
        String targetUrlHost,
        String webhookRef,
        String signingKeyKid,
        Long rawTokenTtlSeconds) {
}
