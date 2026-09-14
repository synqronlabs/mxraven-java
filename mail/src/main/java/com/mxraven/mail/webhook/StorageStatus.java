package com.mxraven.mail.webhook;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * An object-storage {@code NOTIFY_WEBHOOK} delivery status, identified by
 * {@code event_type: "s3_egress_status"}.
 *
 * @param eventType      always {@code "s3_egress_status"}
 * @param taskId         identifies the storage task
 * @param tenantId       the owning tenant
 * @param listenerId     the source listener
 * @param status         the storage outcome
 * @param attempt        the delivery attempt number, starting at 1
 * @param acceptedAtUtc  Unix time, in seconds, when mxRaven accepted the message
 * @param occurredAtUtc  Unix time, in seconds, when this status was built
 * @param storageRef     identifies the storage integration
 * @param bucketName     the destination bucket
 * @param objectKey      the stored object key
 * @param endpointHost   the storage endpoint host
 * @param statusCode     the storage provider response status
 * @param errorCode      the storage provider error code
 * @param message        describes the failure, truncated to 512 bytes
 * @param nextRetryAtUtc Unix time, in seconds, of the next attempt for a deferred status
 */
public record StorageStatus(
        String eventType,
        String taskId,
        String tenantId,
        String listenerId,
        StatusOutcome status,
        long attempt,
        Long acceptedAtUtc,
        long occurredAtUtc,
        String storageRef,
        String bucketName,
        String objectKey,
        String endpointHost,
        int statusCode,
        String errorCode,
        String message,
        Long nextRetryAtUtc) implements WebhookEvent {

    @Override
    @JsonIgnore
    public WebhookEventType type() {
        return WebhookEventType.STORAGE_STATUS;
    }
}
