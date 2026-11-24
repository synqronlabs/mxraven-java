package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;
import java.util.List;

/**
 * The outcome of a simulated routing-script run.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ScriptSimulationResult {
    private final int revision;
    private final ScriptIntent terminal;
    private final boolean defaultApplied;
    private final List<ScriptIntent> intents;
    private final List<ScriptSkippedIntent> skipped;
    private final List<String> log;
    private final ScriptFault fault;
    private final List<ScriptHTTPCall> httpCalls;
    private final List<ScriptTraceEntry> trace;
    private final long durationUs;

    /** revision number that ran */
    public int revision() {
        return revision;
    }

    /** the terminal effect the script selected */
    public ScriptIntent terminal() {
        return terminal;
    }

    /** whether the listener-type default applied because the * script staged no terminal, or a submission script * faulted. An MTA script fault defers instead, so it is * false there. */
    public boolean defaultApplied() {
        return defaultApplied;
    }

    /** non-terminal effects that executed with the terminal, in call order */
    public List<ScriptIntent> intents() {
        return intents;
    }

    /** non-terminal effects skipped by an execution-time check */
    public List<ScriptSkippedIntent> skipped() {
        return skipped;
    }

    /** the script's {@code mx.log} lines */
    public List<String> log() {
        return log;
    }

    /** the fault that made the default apply, when one occurred */
    public ScriptFault fault() {
        return fault;
    }

    /** every {@code mx.http} audit record */
    public List<ScriptHTTPCall> httpCalls() {
        return httpCalls;
    }

    /** host-operation trace */
    public List<ScriptTraceEntry> trace() {
        return trace;
    }

    public long durationUs() {
        return durationUs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScriptSimulationResult that = (ScriptSimulationResult) o;
        return this.revision == that.revision && Objects.equals(this.terminal, that.terminal) && this.defaultApplied == that.defaultApplied && Objects.equals(this.intents, that.intents) && Objects.equals(this.skipped, that.skipped) && Objects.equals(this.log, that.log) && Objects.equals(this.fault, that.fault) && Objects.equals(this.httpCalls, that.httpCalls) && Objects.equals(this.trace, that.trace) && this.durationUs == that.durationUs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.revision, this.terminal, this.defaultApplied, this.intents, this.skipped, this.log, this.fault, this.httpCalls, this.trace, this.durationUs);
    }

    @Override
    public String toString() {
        return "ScriptSimulationResult[" + "revision=" + this.revision + ", " + "terminal=" + this.terminal + ", " + "defaultApplied=" + this.defaultApplied + ", " + "intents=" + this.intents + ", " + "skipped=" + this.skipped + ", " + "log=" + this.log + ", " + "fault=" + this.fault + ", " + "httpCalls=" + this.httpCalls + ", " + "trace=" + this.trace + ", " + "durationUs=" + this.durationUs + "]";
    }

    /**
     * Creates a new ScriptSimulationResult.
     *
     * @param revision revision number that ran
     * @param terminal the terminal effect the script selected
     * @param defaultApplied whether the listener-type default applied because the * script staged no terminal, or a submission script * faulted. An MTA script fault defers instead, so it is * false there.
     * @param intents non-terminal effects that executed with the terminal, in call order
     * @param skipped non-terminal effects skipped by an execution-time check
     * @param log the script's {@code mx.log} lines
     * @param fault the fault that made the default apply, when one occurred
     * @param httpCalls every {@code mx.http} audit record
     * @param trace host-operation trace
     * @param durationUs durationUs
     */
    @JsonCreator
    public ScriptSimulationResult(int revision, ScriptIntent terminal, boolean defaultApplied, List<ScriptIntent> intents, List<ScriptSkippedIntent> skipped, List<String> log, ScriptFault fault, List<ScriptHTTPCall> httpCalls, List<ScriptTraceEntry> trace, long durationUs) {
        this.revision = revision;
        this.terminal = terminal;
        this.defaultApplied = defaultApplied;
        this.fault = fault;
        this.durationUs = durationUs;
        this.intents = intents == null ? Java8.list() : Java8.copyList(intents);
        this.skipped = skipped == null ? Java8.list() : Java8.copyList(skipped);
        this.log = log == null ? Java8.list() : Java8.copyList(log);
        this.httpCalls = httpCalls == null ? Java8.list() : Java8.copyList(httpCalls);
        this.trace = trace == null ? Java8.list() : Java8.copyList(trace);
    }

    /**
         * Creates a result, copying every list.
         */
}
