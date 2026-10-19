package com.mxraven.mail.notify;

import com.mxraven.mail.webhook.StatusOutcome;

import java.util.Objects;

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
 */
public final class AttemptEvent {
    private final String eventType;
    private final AttemptChannel channel;
    private final String taskId;
    private final String tenantId;
    private final String listenerId;
    private final String correlationId;
    private final StatusOutcome status;
    private final long attempt;
    private final Long acceptedAtUtc;
    private final long occurredAtUtc;
    private final String sourceIp;
    private final String destinationDomain;
    private final String remoteHost;
    private final Integer smtpCode;
    private final String enhancedStatusCode;
    private final String reason;
    private final String remoteResponse;
    private final String correlationTaskId;
    private final String storageRef;
    private final String bucketName;
    private final String objectKey;
    private final String endpointHost;
    private final String errorCode;
    private final Integer statusCode;
    private final String message;
    private final Long nextRetryAtUtc;
    private final Long retryAfterMs;
    private final String targetUrlHost;
    private final String webhookRef;
    private final String signingKeyKid;
    private final Long rawTokenTtlSeconds;

    /** the producer's event-type marker, when one is set; SMTP attempts omit it */
    public String eventType() {
        return eventType;
    }

    /** the egress worker that produced the attempt */
    public AttemptChannel channel() {
        return channel;
    }

    /** the delivery task; for a submission listener this equals the accepted event's task id */
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

    /** the task id of the accepted event for the message, when one was recorded */
    public String correlationId() {
        return correlationId;
    }

    /** the attempt outcome */
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

    /** Unix time, in seconds, when the attempt was built */
    public long occurredAtUtc() {
        return occurredAtUtc;
    }

    /** the SMTP egress source address, when known */
    public String sourceIp() {
        return sourceIp;
    }

    /** the recipient domain for an SMTP attempt */
    public String destinationDomain() {
        return destinationDomain;
    }

    /** the remote MTA hostname, when known */
    public String remoteHost() {
        return remoteHost;
    }

    /** the remote SMTP reply code */
    public Integer smtpCode() {
        return smtpCode;
    }

    /** the RFC 3463 enhanced status code */
    public String enhancedStatusCode() {
        return enhancedStatusCode;
    }

    /** the bounded, machine-readable delivery classification */
    public String reason() {
        return reason;
    }

    /** the remote reply text, truncated by the server */
    public String remoteResponse() {
        return remoteResponse;
    }

    /** links a generated DSN back to its original task */
    public String correlationTaskId() {
        return correlationTaskId;
    }

    /** the storage integration identifier for an {@code s3} attempt */
    public String storageRef() {
        return storageRef;
    }

    /** the destination bucket for an {@code s3} attempt */
    public String bucketName() {
        return bucketName;
    }

    /** the stored object key for an {@code s3} attempt */
    public String objectKey() {
        return objectKey;
    }

    /** the storage endpoint host for an {@code s3} attempt */
    public String endpointHost() {
        return endpointHost;
    }

    /** the storage provider error code, when one was returned */
    public String errorCode() {
        return errorCode;
    }

    /** the storage or webhook HTTP response status */
    public Integer statusCode() {
        return statusCode;
    }

    /** the failure description, truncated by the server */
    public String message() {
        return message;
    }

    /** Unix time, in seconds, of the next attempt when the attempt will be retried */
    public Long nextRetryAtUtc() {
        return nextRetryAtUtc;
    }

    /** the webhook retry-after hint, in milliseconds */
    public Long retryAfterMs() {
        return retryAfterMs;
    }

    /** the webhook target host */
    public String targetUrlHost() {
        return targetUrlHost;
    }

    /** the webhook endpoint identifier */
    public String webhookRef() {
        return webhookRef;
    }

    /** the key ID used to sign the webhook attempt */
    public String signingKeyKid() {
        return signingKeyKid;
    }

    /** the raw-message token lifetime the webhook attempt used, in seconds */
    public Long rawTokenTtlSeconds() {
        return rawTokenTtlSeconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttemptEvent that = (AttemptEvent) o;
        return Objects.equals(this.eventType, that.eventType)
                && Objects.equals(this.channel, that.channel)
                && Objects.equals(this.taskId, that.taskId)
                && Objects.equals(this.tenantId, that.tenantId)
                && Objects.equals(this.listenerId, that.listenerId)
                && Objects.equals(this.correlationId, that.correlationId)
                && Objects.equals(this.status, that.status)
                && this.attempt == that.attempt
                && Objects.equals(this.acceptedAtUtc, that.acceptedAtUtc)
                && this.occurredAtUtc == that.occurredAtUtc
                && Objects.equals(this.sourceIp, that.sourceIp)
                && Objects.equals(this.destinationDomain, that.destinationDomain)
                && Objects.equals(this.remoteHost, that.remoteHost)
                && Objects.equals(this.smtpCode, that.smtpCode)
                && Objects.equals(this.enhancedStatusCode, that.enhancedStatusCode)
                && Objects.equals(this.reason, that.reason)
                && Objects.equals(this.remoteResponse, that.remoteResponse)
                && Objects.equals(this.correlationTaskId, that.correlationTaskId)
                && Objects.equals(this.storageRef, that.storageRef)
                && Objects.equals(this.bucketName, that.bucketName)
                && Objects.equals(this.objectKey, that.objectKey)
                && Objects.equals(this.endpointHost, that.endpointHost)
                && Objects.equals(this.errorCode, that.errorCode)
                && Objects.equals(this.statusCode, that.statusCode)
                && Objects.equals(this.message, that.message)
                && Objects.equals(this.nextRetryAtUtc, that.nextRetryAtUtc)
                && Objects.equals(this.retryAfterMs, that.retryAfterMs)
                && Objects.equals(this.targetUrlHost, that.targetUrlHost)
                && Objects.equals(this.webhookRef, that.webhookRef)
                && Objects.equals(this.signingKeyKid, that.signingKeyKid)
                && Objects.equals(this.rawTokenTtlSeconds, that.rawTokenTtlSeconds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.eventType, this.channel, this.taskId, this.tenantId, this.listenerId, this.correlationId, this.status, this.attempt, this.acceptedAtUtc, this.occurredAtUtc, this.sourceIp, this.destinationDomain, this.remoteHost, this.smtpCode, this.enhancedStatusCode, this.reason, this.remoteResponse, this.correlationTaskId, this.storageRef, this.bucketName, this.objectKey, this.endpointHost, this.errorCode, this.statusCode, this.message, this.nextRetryAtUtc, this.retryAfterMs, this.targetUrlHost, this.webhookRef, this.signingKeyKid, this.rawTokenTtlSeconds);
    }

    @Override
    public String toString() {
        return "AttemptEvent[" + "eventType=" + this.eventType + ", " + "channel=" + this.channel + ", " + "taskId=" + this.taskId + ", " + "tenantId=" + this.tenantId + ", " + "listenerId=" + this.listenerId + ", " + "correlationId=" + this.correlationId + ", " + "status=" + this.status + ", " + "attempt=" + this.attempt + ", " + "acceptedAtUtc=" + this.acceptedAtUtc + ", " + "occurredAtUtc=" + this.occurredAtUtc + ", " + "sourceIp=" + this.sourceIp + ", " + "destinationDomain=" + this.destinationDomain + ", " + "remoteHost=" + this.remoteHost + ", " + "smtpCode=" + this.smtpCode + ", " + "enhancedStatusCode=" + this.enhancedStatusCode + ", " + "reason=" + this.reason + ", " + "remoteResponse=" + this.remoteResponse + ", " + "correlationTaskId=" + this.correlationTaskId + ", " + "storageRef=" + this.storageRef + ", " + "bucketName=" + this.bucketName + ", " + "objectKey=" + this.objectKey + ", " + "endpointHost=" + this.endpointHost + ", " + "errorCode=" + this.errorCode + ", " + "statusCode=" + this.statusCode + ", " + "message=" + this.message + ", " + "nextRetryAtUtc=" + this.nextRetryAtUtc + ", " + "retryAfterMs=" + this.retryAfterMs + ", " + "targetUrlHost=" + this.targetUrlHost + ", " + "webhookRef=" + this.webhookRef + ", " + "signingKeyKid=" + this.signingKeyKid + ", " + "rawTokenTtlSeconds=" + this.rawTokenTtlSeconds + "]";
    }

    /**
     * Creates a new AttemptEvent.
     *
     * @param eventType the producer's event-type marker, when one is set; SMTP attempts omit it
     * @param channel the egress worker that produced the attempt
     * @param taskId the delivery task; for a submission listener this equals the accepted event's task id
     * @param tenantId the owning tenant
     * @param listenerId the source listener
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
     * @param storageRef the storage integration identifier for an s3 attempt
     * @param bucketName the destination bucket for an s3 attempt
     * @param objectKey the stored object key for an s3 attempt
     * @param endpointHost the storage endpoint host for an s3 attempt
     * @param errorCode the storage provider error code, when one was returned
     * @param statusCode the storage or webhook HTTP response status
     * @param message the failure description, truncated by the server
     * @param nextRetryAtUtc Unix time, in seconds, of the next attempt when the attempt will be retried
     * @param retryAfterMs the webhook retry-after hint, in milliseconds
     * @param targetUrlHost the webhook target host
     * @param webhookRef the webhook endpoint identifier
     * @param signingKeyKid the key ID used to sign the webhook attempt
     * @param rawTokenTtlSeconds the raw-message token lifetime the webhook attempt used, in seconds
     */
    public AttemptEvent(String eventType, AttemptChannel channel, String taskId, String tenantId, String listenerId, String correlationId, StatusOutcome status, long attempt, Long acceptedAtUtc, long occurredAtUtc, String sourceIp, String destinationDomain, String remoteHost, Integer smtpCode, String enhancedStatusCode, String reason, String remoteResponse, String correlationTaskId, String storageRef, String bucketName, String objectKey, String endpointHost, String errorCode, Integer statusCode, String message, Long nextRetryAtUtc, Long retryAfterMs, String targetUrlHost, String webhookRef, String signingKeyKid, Long rawTokenTtlSeconds) {
        this.eventType = eventType;
        this.channel = channel;
        this.taskId = taskId;
        this.tenantId = tenantId;
        this.listenerId = listenerId;
        this.correlationId = correlationId;
        this.status = status;
        this.attempt = attempt;
        this.acceptedAtUtc = acceptedAtUtc;
        this.occurredAtUtc = occurredAtUtc;
        this.sourceIp = sourceIp;
        this.destinationDomain = destinationDomain;
        this.remoteHost = remoteHost;
        this.smtpCode = smtpCode;
        this.enhancedStatusCode = enhancedStatusCode;
        this.reason = reason;
        this.remoteResponse = remoteResponse;
        this.correlationTaskId = correlationTaskId;
        this.storageRef = storageRef;
        this.bucketName = bucketName;
        this.objectKey = objectKey;
        this.endpointHost = endpointHost;
        this.errorCode = errorCode;
        this.statusCode = statusCode;
        this.message = message;
        this.nextRetryAtUtc = nextRetryAtUtc;
        this.retryAfterMs = retryAfterMs;
        this.targetUrlHost = targetUrlHost;
        this.webhookRef = webhookRef;
        this.signingKeyKid = signingKeyKid;
        this.rawTokenTtlSeconds = rawTokenTtlSeconds;
    }
}
