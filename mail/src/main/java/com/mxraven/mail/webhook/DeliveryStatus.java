package com.mxraven.mail.webhook;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * An SMTP {@code NOTIFY_WEBHOOK} delivery status.
 *
 * <p>These callbacks are best-effort: they are not retried and not ordered. A
 * single {@link #taskId()} can produce many statuses, so deduplicate on the task
 * id together with {@link #status()}, {@link #attempt()}, and
 * {@link #occurredAtUtc()}.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class DeliveryStatus implements WebhookEvent {
    private final String taskId;
    private final String tenantId;
    private final String listenerId;
    private final StatusOutcome status;
    private final long attempt;
    private final long acceptedAtUtc;
    private final long occurredAtUtc;
    private final String sourceIp;
    private final String destinationDomain;
    private final String remoteHost;
    private final int smtpCode;
    private final String enhancedStatusCode;
    private final String remoteResponse;
    private final Long nextRetryAtUtc;
    private final String correlationTaskId;

    /** identifies the delivery task; stable across attempts */
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

    /** the delivery outcome */
    public StatusOutcome status() {
        return status;
    }

    /** the delivery attempt number, starting at 1 */
    public long attempt() {
        return attempt;
    }

    /** Unix time, in seconds, when mxRaven accepted the message */
    public long acceptedAtUtc() {
        return acceptedAtUtc;
    }

    /** Unix time, in seconds, when this status was built */
    public long occurredAtUtc() {
        return occurredAtUtc;
    }

    /** the egress source address, when known */
    public String sourceIp() {
        return sourceIp;
    }

    /** the recipient domain */
    public String destinationDomain() {
        return destinationDomain;
    }

    /** the remote MTA hostname, when known */
    public String remoteHost() {
        return remoteHost;
    }

    /** the remote SMTP reply code */
    public int smtpCode() {
        return smtpCode;
    }

    /** the RFC 3463 enhanced status code */
    public String enhancedStatusCode() {
        return enhancedStatusCode;
    }

    /** the remote reply text, truncated to 512 bytes */
    public String remoteResponse() {
        return remoteResponse;
    }

    /** Unix time, in seconds, of the next attempt for a deferred status */
    public Long nextRetryAtUtc() {
        return nextRetryAtUtc;
    }

    /** links a generated DSN back to its original task */
    public String correlationTaskId() {
        return correlationTaskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeliveryStatus that = (DeliveryStatus) o;
        return Objects.equals(this.taskId, that.taskId)
                && Objects.equals(this.tenantId, that.tenantId)
                && Objects.equals(this.listenerId, that.listenerId)
                && Objects.equals(this.status, that.status)
                && this.attempt == that.attempt
                && this.acceptedAtUtc == that.acceptedAtUtc
                && this.occurredAtUtc == that.occurredAtUtc
                && Objects.equals(this.sourceIp, that.sourceIp)
                && Objects.equals(this.destinationDomain, that.destinationDomain)
                && Objects.equals(this.remoteHost, that.remoteHost)
                && this.smtpCode == that.smtpCode
                && Objects.equals(this.enhancedStatusCode, that.enhancedStatusCode)
                && Objects.equals(this.remoteResponse, that.remoteResponse)
                && Objects.equals(this.nextRetryAtUtc, that.nextRetryAtUtc)
                && Objects.equals(this.correlationTaskId, that.correlationTaskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.taskId, this.tenantId, this.listenerId, this.status, this.attempt, this.acceptedAtUtc, this.occurredAtUtc, this.sourceIp, this.destinationDomain, this.remoteHost, this.smtpCode, this.enhancedStatusCode, this.remoteResponse, this.nextRetryAtUtc, this.correlationTaskId);
    }

    @Override
    public String toString() {
        return "DeliveryStatus[" + "taskId=" + this.taskId + ", " + "tenantId=" + this.tenantId + ", " + "listenerId=" + this.listenerId + ", " + "status=" + this.status + ", " + "attempt=" + this.attempt + ", " + "acceptedAtUtc=" + this.acceptedAtUtc + ", " + "occurredAtUtc=" + this.occurredAtUtc + ", " + "sourceIp=" + this.sourceIp + ", " + "destinationDomain=" + this.destinationDomain + ", " + "remoteHost=" + this.remoteHost + ", " + "smtpCode=" + this.smtpCode + ", " + "enhancedStatusCode=" + this.enhancedStatusCode + ", " + "remoteResponse=" + this.remoteResponse + ", " + "nextRetryAtUtc=" + this.nextRetryAtUtc + ", " + "correlationTaskId=" + this.correlationTaskId + "]";
    }

    /**
     * Creates a new DeliveryStatus.
     *
     * @param taskId identifies the delivery task; stable across attempts
     * @param tenantId the owning tenant
     * @param listenerId the source listener
     * @param status the delivery outcome
     * @param attempt the delivery attempt number, starting at 1
     * @param acceptedAtUtc Unix time, in seconds, when mxRaven accepted the message
     * @param occurredAtUtc Unix time, in seconds, when this status was built
     * @param sourceIp the egress source address, when known
     * @param destinationDomain the recipient domain
     * @param remoteHost the remote MTA hostname, when known
     * @param smtpCode the remote SMTP reply code
     * @param enhancedStatusCode the RFC 3463 enhanced status code
     * @param remoteResponse the remote reply text, truncated to 512 bytes
     * @param nextRetryAtUtc Unix time, in seconds, of the next attempt for a deferred status
     * @param correlationTaskId links a generated DSN back to its original task
     */
    @JsonCreator
    public DeliveryStatus(String taskId, String tenantId, String listenerId, StatusOutcome status, long attempt, long acceptedAtUtc, long occurredAtUtc, String sourceIp, String destinationDomain, String remoteHost, int smtpCode, String enhancedStatusCode, String remoteResponse, Long nextRetryAtUtc, String correlationTaskId) {
        this.taskId = taskId;
        this.tenantId = tenantId;
        this.listenerId = listenerId;
        this.status = status;
        this.attempt = attempt;
        this.acceptedAtUtc = acceptedAtUtc;
        this.occurredAtUtc = occurredAtUtc;
        this.sourceIp = sourceIp;
        this.destinationDomain = destinationDomain;
        this.remoteHost = remoteHost;
        this.smtpCode = smtpCode;
        this.enhancedStatusCode = enhancedStatusCode;
        this.remoteResponse = remoteResponse;
        this.nextRetryAtUtc = nextRetryAtUtc;
        this.correlationTaskId = correlationTaskId;
    }

    @Override
    @JsonIgnore
    public WebhookEventType type() {
        return WebhookEventType.DELIVERY_STATUS;
    }
}
