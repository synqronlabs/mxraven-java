package com.mxraven.mail.notify;

import com.mxraven.mail.internal.Java8;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;
import java.util.List;

/**
 * Reports that an MTA listener's routing script faulted and the message was
 * deferred, so no accepted event exists to carry the fault.
 *
 * <p>Fault events are sampled: each smtp-mta replica publishes at most one per
 * script revision per minute. {@link #suppressedFaults()} counts same-revision
 * faults the producer did not report since its previous fault event, so one
 * sample explains a broken revision without flooding the stream.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class FaultEvent {
    private final String tenantId;
    private final String listenerId;
    private final String messageRef;
    private final long occurredAtUtc;
    private final String scriptRevisionId;
    private final int scriptRevision;
    private final String mailFrom;
    private final String recipient;
    private final String smtpResponse;
    private final RoutingFault fault;
    private final List<String> log;
    private final List<RoutingHTTPCall> httpCalls;
    private final long suppressedFaults;

    /** the owning tenant */
    public String tenantId() {
        return tenantId;
    }

    /** the MTA listener whose script faulted */
    public String listenerId() {
        return listenerId;
    }

    /** the mxRaven message reference of the deferred transaction */
    public String messageRef() {
        return messageRef;
    }

    /** Unix time, in seconds, when the fault was reported */
    public long occurredAtUtc() {
        return occurredAtUtc;
    }

    /** the published script revision that faulted */
    public String scriptRevisionId() {
        return scriptRevisionId;
    }

    /** the revision number that faulted */
    public int scriptRevision() {
        return scriptRevision;
    }

    /** the envelope sender; empty for a null reverse-path */
    public String mailFrom() {
        return mailFrom;
    }

    /** the recipient context whose routing faulted */
    public String recipient() {
        return recipient;
    }

    /** the reply the sender received, such as a {@code 451} deferral */
    public String smtpResponse() {
        return smtpResponse;
    }

    /** the stable fault reason, message, and script line */
    public RoutingFault fault() {
        return fault;
    }

    /** the script's {@code mx.log} lines, in order */
    public List<String> log() {
        return log;
    }

    /** every {@code mx.http} call the invocation made */
    public List<RoutingHTTPCall> httpCalls() {
        return httpCalls;
    }

    public long suppressedFaults() {
        return suppressedFaults;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FaultEvent that = (FaultEvent) o;
        return Objects.equals(this.tenantId, that.tenantId) && Objects.equals(this.listenerId, that.listenerId) && Objects.equals(this.messageRef, that.messageRef) && this.occurredAtUtc == that.occurredAtUtc && Objects.equals(this.scriptRevisionId, that.scriptRevisionId) && this.scriptRevision == that.scriptRevision && Objects.equals(this.mailFrom, that.mailFrom) && Objects.equals(this.recipient, that.recipient) && Objects.equals(this.smtpResponse, that.smtpResponse) && Objects.equals(this.fault, that.fault) && Objects.equals(this.log, that.log) && Objects.equals(this.httpCalls, that.httpCalls) && this.suppressedFaults == that.suppressedFaults;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.tenantId, this.listenerId, this.messageRef, this.occurredAtUtc, this.scriptRevisionId, this.scriptRevision, this.mailFrom, this.recipient, this.smtpResponse, this.fault, this.log, this.httpCalls, this.suppressedFaults);
    }

    @Override
    public String toString() {
        return "FaultEvent[" + "tenantId=" + this.tenantId + ", " + "listenerId=" + this.listenerId + ", " + "messageRef=" + this.messageRef + ", " + "occurredAtUtc=" + this.occurredAtUtc + ", " + "scriptRevisionId=" + this.scriptRevisionId + ", " + "scriptRevision=" + this.scriptRevision + ", " + "mailFrom=" + this.mailFrom + ", " + "recipient=" + this.recipient + ", " + "smtpResponse=" + this.smtpResponse + ", " + "fault=" + this.fault + ", " + "log=" + this.log + ", " + "httpCalls=" + this.httpCalls + ", " + "suppressedFaults=" + this.suppressedFaults + "]";
    }

    /**
     * Creates a new FaultEvent.
     *
     * @param tenantId the owning tenant
     * @param listenerId the MTA listener whose script faulted
     * @param messageRef the mxRaven message reference of the deferred transaction
     * @param occurredAtUtc Unix time, in seconds, when the fault was reported
     * @param scriptRevisionId the published script revision that faulted
     * @param scriptRevision the revision number that faulted
     * @param mailFrom the envelope sender; empty for a null reverse-path
     * @param recipient the recipient context whose routing faulted
     * @param smtpResponse the reply the sender received, such as a {@code 451} deferral
     * @param fault the stable fault reason, message, and script line
     * @param log the script's {@code mx.log} lines, in order
     * @param httpCalls every {@code mx.http} call the invocation made
     * @param suppressedFaults suppressedFaults
     */
    @JsonCreator
    public FaultEvent(String tenantId, String listenerId, String messageRef, long occurredAtUtc, String scriptRevisionId, int scriptRevision, String mailFrom, String recipient, String smtpResponse, RoutingFault fault, List<String> log, List<RoutingHTTPCall> httpCalls, long suppressedFaults) {
        this.tenantId = tenantId;
        this.listenerId = listenerId;
        this.messageRef = messageRef;
        this.occurredAtUtc = occurredAtUtc;
        this.scriptRevisionId = scriptRevisionId;
        this.scriptRevision = scriptRevision;
        this.mailFrom = mailFrom;
        this.recipient = recipient;
        this.smtpResponse = smtpResponse;
        this.fault = fault;
        this.suppressedFaults = suppressedFaults;

        this.log = log == null ? Java8.list() : Java8.copyList(log);
        this.httpCalls = httpCalls == null ? Java8.list() : Java8.copyList(httpCalls);
    }

/**
     * Creates a fault event, copying every list.
     */
}
