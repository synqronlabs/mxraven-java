package com.mxraven.mail.webhook;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * The final routing outcome for an inbound message.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class RoutingDecision {
    private final TerminalAction terminalAction;
    private final String matchedRuleId;
    private final boolean usedListenerDefault;

    /** the final terminal action */
    public TerminalAction terminalAction() {
        return terminalAction;
    }

    /** the rule that selected the terminal action, when one matched */
    public String matchedRuleId() {
        return matchedRuleId;
    }

    /** whether the listener default was used because no rule produced a terminal action */
    public boolean usedListenerDefault() {
        return usedListenerDefault;
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
        return Objects.equals(this.terminalAction, that.terminalAction)
                && Objects.equals(this.matchedRuleId, that.matchedRuleId)
                && this.usedListenerDefault == that.usedListenerDefault;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.terminalAction, this.matchedRuleId, this.usedListenerDefault);
    }

    @Override
    public String toString() {
        return "RoutingDecision[" + "terminalAction=" + this.terminalAction + ", " + "matchedRuleId=" + this.matchedRuleId + ", " + "usedListenerDefault=" + this.usedListenerDefault + "]";
    }

    /**
     * Creates a new RoutingDecision.
     *
     * @param terminalAction the final terminal action
     * @param matchedRuleId the rule that selected the terminal action, when one matched
     * @param usedListenerDefault whether the listener default was used because no rule produced a terminal action
     */
    @JsonCreator
    public RoutingDecision(TerminalAction terminalAction, String matchedRuleId, boolean usedListenerDefault) {
        this.terminalAction = terminalAction;
        this.matchedRuleId = matchedRuleId;
        this.usedListenerDefault = usedListenerDefault;
    }
}
