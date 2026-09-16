package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Retained metadata for one webhook delivery attempt. Request content, response
 * bodies, and raw upstream errors are not exposed.
 *
 * <p>{@code deliveryKind} is {@code deliver_webhook} or {@code notify_webhook}.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class WebhookDelivery {
    private final String id;
    private final String taskId;
    private final String listenerId;
    private final WebhookDeliveryKind deliveryKind;
    private final String outcome;
    private final long attempt;
    private final Integer statusCode;
    private final String targetUrlHost;
    private final String signingKid;
    private final String occurredAt;

    /** unique delivery identifier */
    public String id() {
        return id;
    }

    /** identifier of the delivery task */
    public String taskId() {
        return taskId;
    }

    /** identifier of the listener that produced the delivery */
    public String listenerId() {
        return listenerId;
    }

    /** kind of webhook delivery */
    public WebhookDeliveryKind deliveryKind() {
        return deliveryKind;
    }

    /** delivery outcome */
    public String outcome() {
        return outcome;
    }

    /** one-based attempt number */
    public long attempt() {
        return attempt;
    }

    /** HTTP response status code, if any */
    public Integer statusCode() {
        return statusCode;
    }

    /** host of the delivery target */
    public String targetUrlHost() {
        return targetUrlHost;
    }

    /** key identifier used to sign the delivery */
    public String signingKid() {
        return signingKid;
    }

    /** timestamp when the attempt occurred */
    public String occurredAt() {
        return occurredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WebhookDelivery that = (WebhookDelivery) o;
        return Objects.equals(this.id, that.id)
                && Objects.equals(this.taskId, that.taskId)
                && Objects.equals(this.listenerId, that.listenerId)
                && Objects.equals(this.deliveryKind, that.deliveryKind)
                && Objects.equals(this.outcome, that.outcome)
                && this.attempt == that.attempt
                && Objects.equals(this.statusCode, that.statusCode)
                && Objects.equals(this.targetUrlHost, that.targetUrlHost)
                && Objects.equals(this.signingKid, that.signingKid)
                && Objects.equals(this.occurredAt, that.occurredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.taskId, this.listenerId, this.deliveryKind, this.outcome, this.attempt, this.statusCode, this.targetUrlHost, this.signingKid, this.occurredAt);
    }

    @Override
    public String toString() {
        return "WebhookDelivery[" + "id=" + this.id + ", " + "taskId=" + this.taskId + ", " + "listenerId=" + this.listenerId + ", " + "deliveryKind=" + this.deliveryKind + ", " + "outcome=" + this.outcome + ", " + "attempt=" + this.attempt + ", " + "statusCode=" + this.statusCode + ", " + "targetUrlHost=" + this.targetUrlHost + ", " + "signingKid=" + this.signingKid + ", " + "occurredAt=" + this.occurredAt + "]";
    }

    /**
     * Creates a new WebhookDelivery.
     *
     * @param id unique delivery identifier
     * @param taskId identifier of the delivery task
     * @param listenerId identifier of the listener that produced the delivery
     * @param deliveryKind kind of webhook delivery
     * @param outcome delivery outcome
     * @param attempt one-based attempt number
     * @param statusCode HTTP response status code, if any
     * @param targetUrlHost host of the delivery target
     * @param signingKid key identifier used to sign the delivery
     * @param occurredAt timestamp when the attempt occurred
     */
    @JsonCreator
    public WebhookDelivery(String id, String taskId, String listenerId, WebhookDeliveryKind deliveryKind, String outcome, long attempt, Integer statusCode, String targetUrlHost, String signingKid, String occurredAt) {
        this.id = id;
        this.taskId = taskId;
        this.listenerId = listenerId;
        this.deliveryKind = deliveryKind;
        this.outcome = outcome;
        this.attempt = attempt;
        this.statusCode = statusCode;
        this.targetUrlHost = targetUrlHost;
        this.signingKid = signingKid;
        this.occurredAt = occurredAt;
    }
}
