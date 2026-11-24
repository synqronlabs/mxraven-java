package com.mxraven.mail.notify;

import com.mxraven.mail.internal.Java8;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;
import java.util.List;

/**
 * What a listener's routing script did for one accepted message.
 *
 * <p>It contains the terminal action, the revision that produced it, the
 * executed and skipped non-terminal effects, the script's own log lines, and
 * any fault, so a tenant can debug routing from the event stream alone.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class RoutingDecision {
    private final TerminalAction terminalAction;
    private final boolean usedListenerDefault;
    private final String scriptRevisionId;
    private final int scriptRevision;
    private final List<RoutingIntent> intents;
    private final List<RoutingSkip> skipped;
    private final List<String> log;
    private final RoutingFault fault;
    private final List<RoutingHTTPCall> httpCalls;

    /** the resolved terminal action */
    public TerminalAction terminalAction() {
        return terminalAction;
    }

    /** whether the listener-type default applied because * no script ran or the script staged no terminal * (an MTA script fault defers and reports a * {@link FaultEvent} instead) */
    public boolean usedListenerDefault() {
        return usedListenerDefault;
    }

    /** the published revision that ran; empty when none ran */
    public String scriptRevisionId() {
        return scriptRevisionId;
    }

    /** the published revision number; zero when none ran */
    public int scriptRevision() {
        return scriptRevision;
    }

    /** non-terminal effects that executed with the terminal, in call order */
    public List<RoutingIntent> intents() {
        return intents;
    }

    /** non-terminal effects skipped by an execution-time check */
    public List<RoutingSkip> skipped() {
        return skipped;
    }

    /** the script's {@code mx.log} lines */
    public List<String> log() {
        return log;
    }

    /** the fault that made the listener-type default apply */
    public RoutingFault fault() {
        return fault;
    }

    public List<RoutingHTTPCall> httpCalls() {
        return httpCalls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoutingDecision that = (RoutingDecision) o;
        return Objects.equals(this.terminalAction, that.terminalAction) && this.usedListenerDefault == that.usedListenerDefault && Objects.equals(this.scriptRevisionId, that.scriptRevisionId) && this.scriptRevision == that.scriptRevision && Objects.equals(this.intents, that.intents) && Objects.equals(this.skipped, that.skipped) && Objects.equals(this.log, that.log) && Objects.equals(this.fault, that.fault) && Objects.equals(this.httpCalls, that.httpCalls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.terminalAction, this.usedListenerDefault, this.scriptRevisionId, this.scriptRevision, this.intents, this.skipped, this.log, this.fault, this.httpCalls);
    }

    @Override
    public String toString() {
        return "RoutingDecision[" + "terminalAction=" + this.terminalAction + ", " + "usedListenerDefault=" + this.usedListenerDefault + ", " + "scriptRevisionId=" + this.scriptRevisionId + ", " + "scriptRevision=" + this.scriptRevision + ", " + "intents=" + this.intents + ", " + "skipped=" + this.skipped + ", " + "log=" + this.log + ", " + "fault=" + this.fault + ", " + "httpCalls=" + this.httpCalls + "]";
    }

    /**
     * Creates a new RoutingDecision.
     *
     * @param terminalAction the resolved terminal action
     * @param usedListenerDefault whether the listener-type default applied because * no script ran or the script staged no terminal * (an MTA script fault defers and reports a * {@link FaultEvent} instead)
     * @param scriptRevisionId the published revision that ran; empty when none ran
     * @param scriptRevision the published revision number; zero when none ran
     * @param intents non-terminal effects that executed with the terminal, in call order
     * @param skipped non-terminal effects skipped by an execution-time check
     * @param log the script's {@code mx.log} lines
     * @param fault the fault that made the listener-type default apply
     * @param httpCalls httpCalls
     */
    @JsonCreator
    public RoutingDecision(TerminalAction terminalAction, boolean usedListenerDefault, String scriptRevisionId, int scriptRevision, List<RoutingIntent> intents, List<RoutingSkip> skipped, List<String> log, RoutingFault fault, List<RoutingHTTPCall> httpCalls) {        this.terminalAction = terminalAction;
        this.usedListenerDefault = usedListenerDefault;
        this.scriptRevisionId = scriptRevisionId;
        this.scriptRevision = scriptRevision;
        this.fault = fault;

        this.intents = intents == null ? Java8.list() : Java8.copyList(intents);
        this.skipped = skipped == null ? Java8.list() : Java8.copyList(skipped);
        this.log = log == null ? Java8.list() : Java8.copyList(log);
        this.httpCalls = httpCalls == null ? Java8.list() : Java8.copyList(httpCalls);
    }

/**
     * Creates a decision, copying every list.
     */
}
