package com.mxraven.mail.notify;

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
 * @param suppressedFaults same-revision faults the producer did not report
 */
public record FaultEvent(
        String tenantId,
        String listenerId,
        String messageRef,
        long occurredAtUtc,
        String scriptRevisionId,
        int scriptRevision,
        String mailFrom,
        String recipient,
        String smtpResponse,
        RoutingFault fault,
        List<String> log,
        List<RoutingHTTPCall> httpCalls,
        long suppressedFaults) {

    /**
     * Creates a fault event, copying every list.
     */
    public FaultEvent {
        log = log == null ? List.of() : List.copyOf(log);
        httpCalls = httpCalls == null ? List.of() : List.copyOf(httpCalls);
    }
}
