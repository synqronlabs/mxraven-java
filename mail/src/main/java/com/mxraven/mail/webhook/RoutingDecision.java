package com.mxraven.mail.webhook;

/**
 * The final routing outcome for an inbound message.
 *
 * @param terminalAction    the final terminal action
 * @param matchedRuleId     the rule that selected the terminal action, when one matched
 * @param usedListenerDefault whether the listener default was used because no rule
 *                          produced a terminal action
 */
public record RoutingDecision(
        TerminalAction terminalAction,
        String matchedRuleId,
        boolean usedListenerDefault) {
}
