package com.mxraven.mail.notify;

import java.util.List;

/**
 * What a listener's routing script did for one accepted message.
 *
 * <p>It contains the terminal action, the revision that produced it, the
 * executed and skipped non-terminal effects, the script's own log lines, and
 * any fault, so a tenant can debug routing from the event stream alone.
 *
 * @param terminalAction the resolved terminal action
 * @param usedListenerDefault whether the listener-type default applied because
 *                            no script ran or the script staged no terminal
 *                            (an MTA script fault defers and reports a
 *                            {@link FaultEvent} instead)
 * @param scriptRevisionId the published revision that ran; empty when none ran
 * @param scriptRevision the published revision number; zero when none ran
 * @param intents non-terminal effects that executed with the terminal, in call order
 * @param skipped non-terminal effects skipped by an execution-time check
 * @param log the script's {@code mx.log} lines
 * @param fault the fault that made the listener-type default apply
 * @param httpCalls every {@code mx.http} call the invocation made
 */
public record RoutingDecision(
        TerminalAction terminalAction,
        boolean usedListenerDefault,
        String scriptRevisionId,
        int scriptRevision,
        List<RoutingIntent> intents,
        List<RoutingSkip> skipped,
        List<String> log,
        RoutingFault fault,
        List<RoutingHTTPCall> httpCalls) {

    /**
     * Creates a decision, copying every list.
     */
    public RoutingDecision {
        intents = intents == null ? List.of() : List.copyOf(intents);
        skipped = skipped == null ? List.of() : List.copyOf(skipped);
        log = log == null ? List.of() : List.copyOf(log);
        httpCalls = httpCalls == null ? List.of() : List.copyOf(httpCalls);
    }
}
