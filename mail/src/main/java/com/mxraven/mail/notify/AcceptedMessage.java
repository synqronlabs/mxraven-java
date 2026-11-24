package com.mxraven.mail.notify;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.mxraven.mail.internal.Java8;
import com.mxraven.mail.mime.ParsedEmail;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;

/**
 * A message a listener accepted.
 *
 * <p>The event is published after acceptance and after the routing script
 * resolved, including when the terminal action drops the message: acceptance
 * and disposal are separate facts.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class AcceptedMessage {
    private final String taskId;
    private final String tenantId;
    private final String listenerId;
    private final String messageRef;
    private final long attempt;
    private final long acceptedAtUtc;
    private final long occurredAtUtc;
    private final RoutingDecision routingDecision;
    private final Verdicts verdicts;
    private final Envelope envelope;
    private final MessageSummary message;
    private final List<HeaderField> headers;
    private final RawEmail rawEmail;

    /** identifies the delivery task, stable across retries */
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

    /** the mxRaven message reference; for an outbound listener it is the submission reply's message_ref */
    public String messageRef() {
        return messageRef;
    }

    /** the delivery attempt number, starting at 1 */
    public long attempt() {
        return attempt;
    }

    /** Unix time, in seconds, when mxRaven accepted the message */
    public long acceptedAtUtc() {
        return acceptedAtUtc;
    }

    /** Unix time, in seconds, when the event was built */
    public long occurredAtUtc() {
        return occurredAtUtc;
    }

    /** what the listener's routing script did for this message */
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

    /** every message header */
    public List<HeaderField> headers() {
        return headers;
    }

    /** time-limited access to the raw RFC 822 message */
    public RawEmail rawEmail() {
        return rawEmail;
    }

    /**
     * Downloads and parses the full message referenced by {@link #rawEmail()}.
     *
     * @return the parsed message
     * @throws IOException when the download fails
     * @throws NotificationException when the event carries no raw-message access
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
     * @throws IOException when the download fails
     * @throws NotificationException when the event carries no raw-message access
     */
    public ParsedEmail parse(OkHttpClient client) throws IOException {
        if (rawEmail == null) {
            throw new NotificationException("accepted message has no raw message reference");
        }
        return rawEmail.parse(client);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AcceptedMessage that = (AcceptedMessage) o;
        return Objects.equals(this.taskId, that.taskId)
                && Objects.equals(this.tenantId, that.tenantId)
                && Objects.equals(this.listenerId, that.listenerId)
                && Objects.equals(this.messageRef, that.messageRef)
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
        return Objects.hash(this.taskId, this.tenantId, this.listenerId, this.messageRef,
                this.attempt, this.acceptedAtUtc, this.occurredAtUtc, this.routingDecision,
                this.verdicts, this.envelope, this.message, this.headers, this.rawEmail);
    }

    @Override
    public String toString() {
        return "AcceptedMessage[" + "taskId=" + this.taskId + ", " + "tenantId=" + this.tenantId
                + ", " + "listenerId=" + this.listenerId + ", " + "messageRef=" + this.messageRef
                + ", " + "attempt=" + this.attempt + ", " + "acceptedAtUtc=" + this.acceptedAtUtc
                + ", " + "occurredAtUtc=" + this.occurredAtUtc + ", " + "routingDecision="
                + this.routingDecision + ", " + "verdicts=" + this.verdicts + ", " + "envelope="
                + this.envelope + ", " + "message=" + this.message + ", " + "headers=" + this.headers
                + ", " + "rawEmail=" + this.rawEmail + "]";
    }

    /**
     * Creates a new AcceptedMessage.
     *
     * @param taskId identifies the delivery task
     * @param tenantId the owning tenant
     * @param listenerId the listener that accepted the message
     * @param messageRef the mxRaven message reference
     * @param attempt the delivery attempt number
     * @param acceptedAtUtc Unix time, in seconds, when mxRaven accepted the message
     * @param occurredAtUtc Unix time, in seconds, when the event was built
     * @param routingDecision what the listener's routing script did
     * @param verdicts spam and malware scan results, when scanning ran
     * @param envelope the SMTP envelope
     * @param message a summary of the parsed message headers
     * @param headers every message header
     * @param rawEmail time-limited access to the raw RFC 822 message
     */
    @JsonCreator
    public AcceptedMessage(String taskId, String tenantId, String listenerId, String messageRef,
                           long attempt, long acceptedAtUtc, long occurredAtUtc,
                           RoutingDecision routingDecision, Verdicts verdicts, Envelope envelope,
                           MessageSummary message, List<HeaderField> headers, RawEmail rawEmail) {
        this.taskId = taskId;
        this.tenantId = tenantId;
        this.listenerId = listenerId;
        this.messageRef = messageRef;
        this.attempt = attempt;
        this.acceptedAtUtc = acceptedAtUtc;
        this.occurredAtUtc = occurredAtUtc;
        this.routingDecision = routingDecision;
        this.verdicts = verdicts;
        this.envelope = envelope;
        this.message = message;
        this.headers = headers == null ? Java8.list() : Java8.copyList(headers);
        this.rawEmail = rawEmail;
    }
}
