package com.mxraven.mail.webhook;

import com.mxraven.mail.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mxraven.mail.mime.ParsedEmail;

import java.io.IOException;
import okhttp3.OkHttpClient;
import java.util.List;

/**
 * A {@code DELIVER_WEBHOOK} delivery: a complete inbound message.
 *
 * <p>Delivery is at-least-once. {@link #taskId()} is stable across retries, so
 * deduplicate on it. {@link #attempt()} starts at 1.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class InboundEmail implements WebhookEvent {
    private final String eventType;
    private final String taskId;
    private final String tenantId;
    private final String listenerId;
    private final long attempt;
    private final long acceptedAtUtc;
    private final long occurredAtUtc;
    private final RoutingDecision routingDecision;
    private final Verdicts verdicts;
    private final Envelope envelope;
    private final MessageSummary message;
    private final List<HeaderField> headers;
    private final RawEmail rawEmail;

    /** always {@code "inbound_email"} */
    public String eventType() {
        return eventType;
    }

    /** identifies the delivery task; stable across retries */
    public String taskId() {
        return taskId;
    }

    /** the owning tenant */
    public String tenantId() {
        return tenantId;
    }

    /** the listener that accepted the message */
    public String listenerId() {
        return listenerId;
    }

    /** the delivery attempt number, starting at 1 */
    public long attempt() {
        return attempt;
    }

    /** Unix time, in seconds, when mxRaven accepted the message */
    public long acceptedAtUtc() {
        return acceptedAtUtc;
    }

    /** Unix time, in seconds, when this delivery was built */
    public long occurredAtUtc() {
        return occurredAtUtc;
    }

    /** the final routing outcome */
    public RoutingDecision routingDecision() {
        return routingDecision;
    }

    /** spam and malware scan results, when scanning ran */
    public Verdicts verdicts() {
        return verdicts;
    }

    /** the SMTP envelope */
    public Envelope envelope() {
        return envelope;
    }

    /** a summary of the parsed message headers */
    public MessageSummary message() {
        return message;
    }

    /** every message header, in the order received */
    public List<HeaderField> headers() {
        return headers;
    }

    /** time-limited access to the raw RFC 822 message */
    public RawEmail rawEmail() {
        return rawEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InboundEmail that = (InboundEmail) o;
        return Objects.equals(this.eventType, that.eventType)
                && Objects.equals(this.taskId, that.taskId)
                && Objects.equals(this.tenantId, that.tenantId)
                && Objects.equals(this.listenerId, that.listenerId)
                && this.attempt == that.attempt
                && this.acceptedAtUtc == that.acceptedAtUtc
                && this.occurredAtUtc == that.occurredAtUtc
                && Objects.equals(this.routingDecision, that.routingDecision)
                && Objects.equals(this.verdicts, that.verdicts)
                && Objects.equals(this.envelope, that.envelope)
                && Objects.equals(this.message, that.message)
                && Objects.equals(this.headers, that.headers)
                && Objects.equals(this.rawEmail, that.rawEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.eventType, this.taskId, this.tenantId, this.listenerId, this.attempt, this.acceptedAtUtc, this.occurredAtUtc, this.routingDecision, this.verdicts, this.envelope, this.message, this.headers, this.rawEmail);
    }

    @Override
    public String toString() {
        return "InboundEmail[" + "eventType=" + this.eventType + ", " + "taskId=" + this.taskId + ", " + "tenantId=" + this.tenantId + ", " + "listenerId=" + this.listenerId + ", " + "attempt=" + this.attempt + ", " + "acceptedAtUtc=" + this.acceptedAtUtc + ", " + "occurredAtUtc=" + this.occurredAtUtc + ", " + "routingDecision=" + this.routingDecision + ", " + "verdicts=" + this.verdicts + ", " + "envelope=" + this.envelope + ", " + "message=" + this.message + ", " + "headers=" + this.headers + ", " + "rawEmail=" + this.rawEmail + "]";
    }

    /**
     * Creates an inbound email delivery, copying the header list.
     */
    @JsonCreator
    public InboundEmail(String eventType, String taskId, String tenantId, String listenerId, long attempt, long acceptedAtUtc, long occurredAtUtc, RoutingDecision routingDecision, Verdicts verdicts, Envelope envelope, MessageSummary message, List<HeaderField> headers, RawEmail rawEmail) {

        headers = headers == null ? Java8.list() : Java8.copyList(headers);
    
        this.eventType = eventType;
        this.taskId = taskId;
        this.tenantId = tenantId;
        this.listenerId = listenerId;
        this.attempt = attempt;
        this.acceptedAtUtc = acceptedAtUtc;
        this.occurredAtUtc = occurredAtUtc;
        this.routingDecision = routingDecision;
        this.verdicts = verdicts;
        this.envelope = envelope;
        this.message = message;
        this.headers = headers;
        this.rawEmail = rawEmail;
    }

    @Override
    @JsonIgnore
    public WebhookEventType type() {
        return WebhookEventType.INBOUND_EMAIL;
    }

    /**
     * Downloads and parses the full message referenced by {@link #rawEmail()}.
     *
     * @return the parsed message
     * @throws IOException      when the download fails
     * @throws WebhookException when the delivery carries no raw message reference
     */
    public ParsedEmail parse() throws IOException {
        return parse(null);
    }

    /**
     * Downloads with {@code client} and parses the full message referenced by
     * {@link #rawEmail()}.
     *
     * @param client the HTTP client to download with, or {@code null} for a default client
     * @return the parsed message
     * @throws IOException      when the download fails
     * @throws WebhookException when the delivery carries no raw message reference
     */
    public ParsedEmail parse(OkHttpClient client) throws IOException {
        if (rawEmail == null) {
            throw new WebhookException("inbound email has no raw message reference");
        }
        return rawEmail.parse(client);
    }
}
