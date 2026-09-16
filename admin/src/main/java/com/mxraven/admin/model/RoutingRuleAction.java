package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * A routing rule action: a discriminated union keyed on {@link #actionType()}.
 *
 * <p>Build one with the per-action factories or the fluent builder. Factories validate the payload
 * shape and values locally, matching the control plane.
 *
 * <pre>{@code
 * CreateRoutingRuleRequest.builder()
 *         .priority(1)
 *         .expressionText("from == 'billing@example.com'")
 *         .action(RoutingRuleAction.smtpForward("billing-dest"))
 *         .build();
 * }</pre>
 */
public final class RoutingRuleAction {
    private final RoutingRuleActionKind actionType;
    private final RoutingRulePayload actionPayload;

    private RoutingRuleAction(RoutingRuleActionKind actionType, RoutingRulePayload actionPayload) {
        this.actionType = actionType;
        this.actionPayload = actionPayload;
    }

    /**
     * Creates an action from an explicit kind and payload.
     *
     * @param actionType    kind of action
     * @param actionPayload action-specific payload
     * @return a new action
     */
    @JsonCreator
    public static RoutingRuleAction of(@JsonProperty("action_type") RoutingRuleActionKind actionType,
                                       @JsonProperty("action_payload") RoutingRulePayload actionPayload) {
        return new RoutingRuleAction(actionType, actionPayload);
    }

    /**
     * Returns the kind of action.
     *
     * @return the kind of action
     */
    @JsonProperty("action_type")
    public RoutingRuleActionKind actionType() {
        return actionType;
    }

    /**
     * Returns the action-specific payload.
     *
     * @return the action-specific payload
     */
    @JsonProperty("action_payload")
    public RoutingRulePayload actionPayload() {
        return actionPayload;
    }

    /**
     * Creates a new builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link RoutingRuleAction} instances from an action kind and payload. */
    public static final class Builder {
        private RoutingRuleActionKind actionType;
        private RoutingRulePayload actionPayload;

        /**
         * Sets the kind of action.
         *
         * @param actionType kind of action
         * @return this builder
         */
        public Builder actionType(RoutingRuleActionKind actionType) {
            this.actionType = actionType;
            return this;
        }

        /**
         * Sets the action-specific payload.
         *
         * @param actionPayload action-specific payload
         * @return this builder
         */
        public Builder payload(RoutingRulePayload actionPayload) {
            this.actionPayload = actionPayload;
            return this;
        }

        /**
         * Sets the action-specific payload.
         *
         * @param actionPayload action-specific payload
         * @return this builder
         */
        public Builder actionPayload(RoutingRulePayload actionPayload) {
            return payload(actionPayload);
        }

        /**
         * Builds the action.
         *
         * @return a new action
         * @throws IllegalArgumentException if the kind or payload is missing or mismatched
         */
        public RoutingRuleAction build() {
            RoutingRuleActionKind resolved = actionType != null ? actionType
                    : actionPayload == null ? null : actionPayload.kind();
            if (resolved == null) {
                throw new IllegalArgumentException("action_type is required");
            }
            if (actionPayload == null) {
                throw new IllegalArgumentException("action_payload is required");
            }
            if (actionType != null && actionPayload.kind() != null && actionType != actionPayload.kind()) {
                throw new IllegalArgumentException("action_type " + actionType
                        + " does not match the payload for " + actionPayload.kind());
            }
            return new RoutingRuleAction(resolved, actionPayload);
        }
    }

    // --- factories -----------------------------------------------------------

    /**
     * Creates a deliver-to-dedicated-pool action.
     *
     * @param poolId identifier of the dedicated IP pool
     * @return the action
     * @throws IllegalArgumentException if {@code poolId} is not a UUID
     */
    public static RoutingRuleAction deliverDedicated(String poolId) {
        return wrap(RoutingRulePayload.deliverDedicated(poolId));
    }

    /**
     * Creates a default delivery action.
     *
     * @return the action
     */
    public static RoutingRuleAction deliver() {
        return wrap(RoutingRulePayload.deliver());
    }

    /**
     * Creates a smart-host relay action.
     *
     * @param relayRef reference of the smart host
     * @return the action
     * @throws IllegalArgumentException if {@code relayRef} is missing or too long
     */
    public static RoutingRuleAction smartHostRelay(String relayRef) {
        return wrap(RoutingRulePayload.smartHostRelay(relayRef));
    }

    /**
     * Creates a relay action.
     *
     * @param relayRef reference of the relay
     * @return the action
     * @throws IllegalArgumentException if {@code relayRef} is missing or too long
     */
    public static RoutingRuleAction relay(String relayRef) {
        return wrap(RoutingRulePayload.relay(relayRef));
    }

    /**
     * Creates an auto-reply action.
     *
     * @param templateRef reference of the auto-reply template
     * @return the action
     * @throws IllegalArgumentException if {@code templateRef} is missing or too long
     */
    public static RoutingRuleAction autoReply(String templateRef) {
        return wrap(RoutingRulePayload.autoReply(templateRef));
    }

    /**
     * Creates a webhook notification action.
     *
     * @param webhookRef reference of the webhook
     * @return the action
     * @throws IllegalArgumentException if {@code webhookRef} is missing or too long
     */
    public static RoutingRuleAction notifyWebhook(String webhookRef) {
        return wrap(RoutingRulePayload.notifyWebhook(webhookRef));
    }

    /**
     * Creates a webhook delivery action.
     *
     * @param webhookRef reference of the webhook
     * @return the action
     * @throws IllegalArgumentException if {@code webhookRef} is missing or too long
     */
    public static RoutingRuleAction deliverWebhook(String webhookRef) {
        return wrap(RoutingRulePayload.deliverWebhook(webhookRef));
    }

    /**
     * Creates an SMTP forward action.
     *
     * @param destinationRef reference of the destination
     * @return the action
     * @throws IllegalArgumentException if {@code destinationRef} is missing or invalid
     */
    public static RoutingRuleAction smtpForward(String destinationRef) {
        return wrap(RoutingRulePayload.smtpForward(destinationRef));
    }

    /**
     * Creates a drop action.
     *
     * @return the action
     */
    public static RoutingRuleAction drop() {
        return wrap(RoutingRulePayload.drop());
    }

    /**
     * Creates a drop action with an audit reason.
     *
     * @param auditReason reason recorded for the drop
     * @return the action
     * @throws IllegalArgumentException if {@code auditReason} is too long
     */
    public static RoutingRuleAction drop(String auditReason) {
        return wrap(RoutingRulePayload.drop(auditReason));
    }

    /**
     * Creates a reject action.
     *
     * @param smtpStatusCode     SMTP status code in the 5xx range
     * @param enhancedStatusCode enhanced status code of the form {@code 5.subject.detail}
     * @param message            rejection message
     * @return the action
     * @throws IllegalArgumentException if the status codes or message are invalid
     */
    public static RoutingRuleAction reject(int smtpStatusCode, String enhancedStatusCode, String message) {
        return wrap(RoutingRulePayload.reject(smtpStatusCode, enhancedStatusCode, message));
    }

    /**
     * Creates a reject action with an audit reason.
     *
     * @param smtpStatusCode     SMTP status code in the 5xx range
     * @param enhancedStatusCode enhanced status code of the form {@code 5.subject.detail}
     * @param message            rejection message
     * @param auditReason        reason recorded for the rejection
     * @return the action
     * @throws IllegalArgumentException if the status codes, message, or reason are invalid
     */
    public static RoutingRuleAction reject(int smtpStatusCode, String enhancedStatusCode, String message,
                                           String auditReason) {
        return wrap(RoutingRulePayload.reject(smtpStatusCode, enhancedStatusCode, message, auditReason));
    }

    /**
     * Creates a header-modification action.
     *
     * @param operations header operations to apply
     * @return the action
     * @throws IllegalArgumentException if the operations are empty, too many, or invalid
     */
    public static RoutingRuleAction modifyHeader(List<ModifyHeaderOperation> operations) {
        return wrap(RoutingRulePayload.modifyHeader(operations));
    }

    /**
     * Creates an add-recipient action.
     *
     * @param recipients recipient mailbox addresses
     * @return the action
     * @throws IllegalArgumentException if the recipients are empty, too many, or invalid
     */
    public static RoutingRuleAction addRecipient(List<String> recipients) {
        return wrap(RoutingRulePayload.addRecipient(recipients));
    }

    /**
     * Creates an S3 storage action.
     *
     * @param storageRef        reference of the storage backend
     * @param objectKeyPrefix   bucket-relative key prefix ending with {@code /}
     * @param objectKeyTemplate object key template
     * @return the action
     * @throws IllegalArgumentException if the storage reference or key configuration is invalid
     */
    public static RoutingRuleAction s3Store(String storageRef, String objectKeyPrefix, String objectKeyTemplate) {
        return wrap(RoutingRulePayload.s3Store(storageRef, objectKeyPrefix, objectKeyTemplate));
    }

    private static RoutingRuleAction wrap(RoutingRulePayload payload) {
        return new RoutingRuleAction(payload.kind(), payload);
    }
}
