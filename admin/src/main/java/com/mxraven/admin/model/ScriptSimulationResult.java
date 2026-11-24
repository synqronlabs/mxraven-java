package com.mxraven.admin.model;

import java.util.List;

/**
 * The outcome of a simulated routing-script run.
 *
 * @param revision revision number that ran
 * @param terminal the terminal effect the script selected
 * @param defaultApplied whether the listener-type default applied because the
 *                       script staged no terminal, or a submission script
 *                       faulted. An MTA script fault defers instead, so it is
 *                       false there.
 * @param intents non-terminal effects that executed with the terminal, in call order
 * @param skipped non-terminal effects skipped by an execution-time check
 * @param log the script's {@code mx.log} lines
 * @param fault the fault that made the default apply, when one occurred
 * @param httpCalls every {@code mx.http} audit record
 * @param trace host-operation trace
 * @param durationUs simulated run duration in microseconds
 */
public record ScriptSimulationResult(
        int revision,
        ScriptIntent terminal,
        boolean defaultApplied,
        List<ScriptIntent> intents,
        List<ScriptSkippedIntent> skipped,
        List<String> log,
        ScriptFault fault,
        List<ScriptHTTPCall> httpCalls,
        List<ScriptTraceEntry> trace,
        long durationUs) {

    /**
     * Creates a result, copying every list.
     */
    public ScriptSimulationResult {
        intents = intents == null ? List.of() : List.copyOf(intents);
        skipped = skipped == null ? List.of() : List.copyOf(skipped);
        log = log == null ? List.of() : List.copyOf(log);
        httpCalls = httpCalls == null ? List.of() : List.copyOf(httpCalls);
        trace = trace == null ? List.of() : List.copyOf(trace);
    }
}
