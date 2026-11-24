package com.mxraven.admin.model;

/**
 * One inbound recipient outcome that was rejected synchronously or suppressed
 * in a mixed-recipient SMTP transaction.
 *
 * @param id outcome identifier
 * @param listenerId listener that accepted the transaction
 * @param messageRef mxRaven message reference
 * @param recipientOrdinal zero-based recipient position in the transaction
 * @param recipientDomain recipient domain
 * @param scriptRevisionId routing-script revision that produced the rejection, when one ran
 * @param usedListenerDefault whether the listener-type default applied
 * @param terminalAction terminal action that was resolved
 * @param disposition how the outcome was disposed of
 * @param smtpStatusCode SMTP reply code
 * @param enhancedStatusCode RFC 3463 enhanced status code
 * @param auditReason machine-readable audit reason
 * @param createdAt outcome timestamp
 */
public record InboundRoutingOutcome(
        String id,
        String listenerId,
        String messageRef,
        int recipientOrdinal,
        String recipientDomain,
        String scriptRevisionId,
        boolean usedListenerDefault,
        InboundRoutingOutcomeTerminalAction terminalAction,
        InboundRoutingOutcomeDisposition disposition,
        int smtpStatusCode,
        String enhancedStatusCode,
        String auditReason,
        String createdAt) {
}
