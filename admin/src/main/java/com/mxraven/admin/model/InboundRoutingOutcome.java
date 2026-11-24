package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * One inbound recipient outcome that was rejected synchronously or suppressed
 * in a mixed-recipient SMTP transaction.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class InboundRoutingOutcome {
    private final String id;
    private final String listenerId;
    private final String messageRef;
    private final int recipientOrdinal;
    private final String recipientDomain;
    private final String scriptRevisionId;
    private final boolean usedListenerDefault;
    private final InboundRoutingOutcomeTerminalAction terminalAction;
    private final InboundRoutingOutcomeDisposition disposition;
    private final int smtpStatusCode;
    private final String enhancedStatusCode;
    private final String auditReason;
    private final String createdAt;

    /** outcome identifier */
    public String id() {
        return id;
    }

    /** listener that accepted the transaction */
    public String listenerId() {
        return listenerId;
    }

    /** mxRaven message reference */
    public String messageRef() {
        return messageRef;
    }

    /** zero-based recipient position in the transaction */
    public int recipientOrdinal() {
        return recipientOrdinal;
    }

    /** recipient domain */
    public String recipientDomain() {
        return recipientDomain;
    }

    /** routing-script revision that produced the rejection, when one ran */
    public String scriptRevisionId() {
        return scriptRevisionId;
    }

    /** whether the listener-type default applied */
    public boolean usedListenerDefault() {
        return usedListenerDefault;
    }

    /** terminal action that was resolved */
    public InboundRoutingOutcomeTerminalAction terminalAction() {
        return terminalAction;
    }

    /** how the outcome was disposed of */
    public InboundRoutingOutcomeDisposition disposition() {
        return disposition;
    }

    /** SMTP reply code */
    public int smtpStatusCode() {
        return smtpStatusCode;
    }

    /** RFC 3463 enhanced status code */
    public String enhancedStatusCode() {
        return enhancedStatusCode;
    }

    /** machine-readable audit reason */
    public String auditReason() {
        return auditReason;
    }

    public String createdAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InboundRoutingOutcome that = (InboundRoutingOutcome) o;
        return Objects.equals(this.id, that.id) && Objects.equals(this.listenerId, that.listenerId) && Objects.equals(this.messageRef, that.messageRef) && this.recipientOrdinal == that.recipientOrdinal && Objects.equals(this.recipientDomain, that.recipientDomain) && Objects.equals(this.scriptRevisionId, that.scriptRevisionId) && this.usedListenerDefault == that.usedListenerDefault && Objects.equals(this.terminalAction, that.terminalAction) && Objects.equals(this.disposition, that.disposition) && this.smtpStatusCode == that.smtpStatusCode && Objects.equals(this.enhancedStatusCode, that.enhancedStatusCode) && Objects.equals(this.auditReason, that.auditReason) && Objects.equals(this.createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.listenerId, this.messageRef, this.recipientOrdinal, this.recipientDomain, this.scriptRevisionId, this.usedListenerDefault, this.terminalAction, this.disposition, this.smtpStatusCode, this.enhancedStatusCode, this.auditReason, this.createdAt);
    }

    @Override
    public String toString() {
        return "InboundRoutingOutcome[" + "id=" + this.id + ", " + "listenerId=" + this.listenerId + ", " + "messageRef=" + this.messageRef + ", " + "recipientOrdinal=" + this.recipientOrdinal + ", " + "recipientDomain=" + this.recipientDomain + ", " + "scriptRevisionId=" + this.scriptRevisionId + ", " + "usedListenerDefault=" + this.usedListenerDefault + ", " + "terminalAction=" + this.terminalAction + ", " + "disposition=" + this.disposition + ", " + "smtpStatusCode=" + this.smtpStatusCode + ", " + "enhancedStatusCode=" + this.enhancedStatusCode + ", " + "auditReason=" + this.auditReason + ", " + "createdAt=" + this.createdAt + "]";
    }

    /**
     * Creates a new InboundRoutingOutcome.
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
     * @param createdAt createdAt
     */
    @JsonCreator
    public InboundRoutingOutcome(String id, String listenerId, String messageRef, int recipientOrdinal, String recipientDomain, String scriptRevisionId, boolean usedListenerDefault, InboundRoutingOutcomeTerminalAction terminalAction, InboundRoutingOutcomeDisposition disposition, int smtpStatusCode, String enhancedStatusCode, String auditReason, String createdAt) {
        this.id = id;
        this.listenerId = listenerId;
        this.messageRef = messageRef;
        this.recipientOrdinal = recipientOrdinal;
        this.recipientDomain = recipientDomain;
        this.scriptRevisionId = scriptRevisionId;
        this.usedListenerDefault = usedListenerDefault;
        this.terminalAction = terminalAction;
        this.disposition = disposition;
        this.smtpStatusCode = smtpStatusCode;
        this.enhancedStatusCode = enhancedStatusCode;
        this.auditReason = auditReason;
        this.createdAt = createdAt;
    }
}
