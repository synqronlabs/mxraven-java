package com.mxraven.mail.webhook;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * An object-storage {@code NOTIFY_WEBHOOK} delivery status, identified by
 * {@code event_type: "s3_egress_status"}.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class StorageStatus implements WebhookEvent {
    private final String eventType;
    private final String taskId;
    private final String tenantId;
    private final String listenerId;
    private final StatusOutcome status;
    private final long attempt;
    private final Long acceptedAtUtc;
    private final long occurredAtUtc;
    private final String storageRef;
    private final String bucketName;
    private final String objectKey;
    private final String endpointHost;
    private final int statusCode;
    private final String errorCode;
    private final String message;
    private final Long nextRetryAtUtc;

    /** always {@code "s3_egress_status"} */
    public String eventType() {
        return eventType;
    }

    /** identifies the storage task */
    public String taskId() {
        return taskId;
    }

    /** the owning tenant */
    public String tenantId() {
        return tenantId;
    }

    /** the source listener */
    public String listenerId() {
        return listenerId;
    }

    /** the storage outcome */
    public StatusOutcome status() {
        return status;
    }

    /** the delivery attempt number, starting at 1 */
    public long attempt() {
        return attempt;
    }

    /** Unix time, in seconds, when mxRaven accepted the message */
    public Long acceptedAtUtc() {
        return acceptedAtUtc;
    }

    /** Unix time, in seconds, when this status was built */
    public long occurredAtUtc() {
        return occurredAtUtc;
    }

    /** identifies the storage integration */
    public String storageRef() {
        return storageRef;
    }

    /** the destination bucket */
    public String bucketName() {
        return bucketName;
    }

    /** the stored object key */
    public String objectKey() {
        return objectKey;
    }

    /** the storage endpoint host */
    public String endpointHost() {
        return endpointHost;
    }

    /** the storage provider response status */
    public int statusCode() {
        return statusCode;
    }

    /** the storage provider error code */
    public String errorCode() {
        return errorCode;
    }

    /** describes the failure, truncated to 512 bytes */
    public String message() {
        return message;
    }

    /** Unix time, in seconds, of the next attempt for a deferred status */
    public Long nextRetryAtUtc() {
        return nextRetryAtUtc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StorageStatus that = (StorageStatus) o;
        return Objects.equals(this.eventType, that.eventType)
                && Objects.equals(this.taskId, that.taskId)
                && Objects.equals(this.tenantId, that.tenantId)
                && Objects.equals(this.listenerId, that.listenerId)
                && Objects.equals(this.status, that.status)
                && this.attempt == that.attempt
                && Objects.equals(this.acceptedAtUtc, that.acceptedAtUtc)
                && this.occurredAtUtc == that.occurredAtUtc
                && Objects.equals(this.storageRef, that.storageRef)
                && Objects.equals(this.bucketName, that.bucketName)
                && Objects.equals(this.objectKey, that.objectKey)
                && Objects.equals(this.endpointHost, that.endpointHost)
                && this.statusCode == that.statusCode
                && Objects.equals(this.errorCode, that.errorCode)
                && Objects.equals(this.message, that.message)
                && Objects.equals(this.nextRetryAtUtc, that.nextRetryAtUtc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.eventType, this.taskId, this.tenantId, this.listenerId, this.status, this.attempt, this.acceptedAtUtc, this.occurredAtUtc, this.storageRef, this.bucketName, this.objectKey, this.endpointHost, this.statusCode, this.errorCode, this.message, this.nextRetryAtUtc);
    }

    @Override
    public String toString() {
        return "StorageStatus[" + "eventType=" + this.eventType + ", " + "taskId=" + this.taskId + ", " + "tenantId=" + this.tenantId + ", " + "listenerId=" + this.listenerId + ", " + "status=" + this.status + ", " + "attempt=" + this.attempt + ", " + "acceptedAtUtc=" + this.acceptedAtUtc + ", " + "occurredAtUtc=" + this.occurredAtUtc + ", " + "storageRef=" + this.storageRef + ", " + "bucketName=" + this.bucketName + ", " + "objectKey=" + this.objectKey + ", " + "endpointHost=" + this.endpointHost + ", " + "statusCode=" + this.statusCode + ", " + "errorCode=" + this.errorCode + ", " + "message=" + this.message + ", " + "nextRetryAtUtc=" + this.nextRetryAtUtc + "]";
    }

    /**
     * Creates a new StorageStatus.
     *
     * @param eventType always {@code "s3_egress_status"}
     * @param taskId identifies the storage task
     * @param tenantId the owning tenant
     * @param listenerId the source listener
     * @param status the storage outcome
     * @param attempt the delivery attempt number, starting at 1
     * @param acceptedAtUtc Unix time, in seconds, when mxRaven accepted the message
     * @param occurredAtUtc Unix time, in seconds, when this status was built
     * @param storageRef identifies the storage integration
     * @param bucketName the destination bucket
     * @param objectKey the stored object key
     * @param endpointHost the storage endpoint host
     * @param statusCode the storage provider response status
     * @param errorCode the storage provider error code
     * @param message describes the failure, truncated to 512 bytes
     * @param nextRetryAtUtc Unix time, in seconds, of the next attempt for a deferred status
     */
    @JsonCreator
    public StorageStatus(String eventType, String taskId, String tenantId, String listenerId, StatusOutcome status, long attempt, Long acceptedAtUtc, long occurredAtUtc, String storageRef, String bucketName, String objectKey, String endpointHost, int statusCode, String errorCode, String message, Long nextRetryAtUtc) {
        this.eventType = eventType;
        this.taskId = taskId;
        this.tenantId = tenantId;
        this.listenerId = listenerId;
        this.status = status;
        this.attempt = attempt;
        this.acceptedAtUtc = acceptedAtUtc;
        this.occurredAtUtc = occurredAtUtc;
        this.storageRef = storageRef;
        this.bucketName = bucketName;
        this.objectKey = objectKey;
        this.endpointHost = endpointHost;
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.message = message;
        this.nextRetryAtUtc = nextRetryAtUtc;
    }

    @Override
    @JsonIgnore
    public WebhookEventType type() {
        return WebhookEventType.STORAGE_STATUS;
    }
}
