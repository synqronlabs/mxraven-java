package com.mxraven.mail.webhook;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * An SMTP {@code NOTIFY_WEBHOOK} delivery status.
 *
 * <p>These callbacks are best-effort: they are not retried and not ordered. A
 * single {@link #taskId()} can produce many statuses, so deduplicate on the task
 * id together with {@link #status()}, {@link #attempt()}, and
 * {@link #occurredAtUtc()}.
 *
 * @param taskId             identifies the delivery task; stable across attempts
 * @param tenantId           the owning tenant
 * @param listenerId         the source listener
 * @param status             the delivery outcome
 * @param attempt            the delivery attempt number, starting at 1
 * @param acceptedAtUtc      Unix time, in seconds, when mxRaven accepted the message
 * @param occurredAtUtc      Unix time, in seconds, when this status was built
 * @param sourceIp           the egress source address, when known
 * @param destinationDomain  the recipient domain
 * @param remoteHost         the remote MTA hostname, when known
 * @param smtpCode           the remote SMTP reply code
 * @param enhancedStatusCode the RFC 3463 enhanced status code
 * @param remoteResponse     the remote reply text, truncated to 512 bytes
 * @param nextRetryAtUtc     Unix time, in seconds, of the next attempt for a deferred status
 * @param correlationTaskId  links a generated DSN back to its original task
 */
public record DeliveryStatus(
        String taskId,
        String tenantId,
        String listenerId,
        StatusOutcome status,
        long attempt,
        long acceptedAtUtc,
        long occurredAtUtc,
        String sourceIp,
        String destinationDomain,
        String remoteHost,
        int smtpCode,
        String enhancedStatusCode,
        String remoteResponse,
        Long nextRetryAtUtc,
        String correlationTaskId) implements WebhookEvent {

    @Override
    @JsonIgnore
    public WebhookEventType type() {
        return WebhookEventType.DELIVERY_STATUS;
    }
}
