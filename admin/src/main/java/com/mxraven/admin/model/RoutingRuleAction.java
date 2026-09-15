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

    @JsonCreator
    public static RoutingRuleAction of(@JsonProperty("action_type") RoutingRuleActionKind actionType,
                                       @JsonProperty("action_payload") RoutingRulePayload actionPayload) {
        return new RoutingRuleAction(actionType, actionPayload);
    }

    @JsonProperty("action_type")
    public RoutingRuleActionKind actionType() {
        return actionType;
    }

    @JsonProperty("action_payload")
    public RoutingRulePayload actionPayload() {
        return actionPayload;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private RoutingRuleActionKind actionType;
        private RoutingRulePayload actionPayload;

        public Builder actionType(RoutingRuleActionKind actionType) {
            this.actionType = actionType;
            return this;
        }

        public Builder payload(RoutingRulePayload actionPayload) {
            this.actionPayload = actionPayload;
            return this;
        }

        public Builder actionPayload(RoutingRulePayload actionPayload) {
            return payload(actionPayload);
        }

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

    public static RoutingRuleAction deliverDedicated(String poolId) {
        return wrap(RoutingRulePayload.deliverDedicated(poolId));
    }

    public static RoutingRuleAction deliver() {
        return wrap(RoutingRulePayload.deliver());
    }

    public static RoutingRuleAction smartHostRelay(String relayRef) {
        return wrap(RoutingRulePayload.smartHostRelay(relayRef));
    }

    public static RoutingRuleAction relay(String relayRef) {
        return wrap(RoutingRulePayload.relay(relayRef));
    }

    public static RoutingRuleAction autoReply(String templateRef) {
        return wrap(RoutingRulePayload.autoReply(templateRef));
    }

    public static RoutingRuleAction notifyWebhook(String webhookRef) {
        return wrap(RoutingRulePayload.notifyWebhook(webhookRef));
    }

    public static RoutingRuleAction deliverWebhook(String webhookRef) {
        return wrap(RoutingRulePayload.deliverWebhook(webhookRef));
    }

    public static RoutingRuleAction smtpForward(String destinationRef) {
        return wrap(RoutingRulePayload.smtpForward(destinationRef));
    }

    public static RoutingRuleAction drop() {
        return wrap(RoutingRulePayload.drop());
    }

    public static RoutingRuleAction drop(String auditReason) {
        return wrap(RoutingRulePayload.drop(auditReason));
    }

    public static RoutingRuleAction reject(int smtpStatusCode, String enhancedStatusCode, String message) {
        return wrap(RoutingRulePayload.reject(smtpStatusCode, enhancedStatusCode, message));
    }

    public static RoutingRuleAction reject(int smtpStatusCode, String enhancedStatusCode, String message,
                                           String auditReason) {
        return wrap(RoutingRulePayload.reject(smtpStatusCode, enhancedStatusCode, message, auditReason));
    }

    public static RoutingRuleAction modifyHeader(List<ModifyHeaderOperation> operations) {
        return wrap(RoutingRulePayload.modifyHeader(operations));
    }

    public static RoutingRuleAction addRecipient(List<String> recipients) {
        return wrap(RoutingRulePayload.addRecipient(recipients));
    }

    public static RoutingRuleAction s3Store(String storageRef, String objectKeyPrefix, String objectKeyTemplate) {
        return wrap(RoutingRulePayload.s3Store(storageRef, objectKeyPrefix, objectKeyTemplate));
    }

    private static RoutingRuleAction wrap(RoutingRulePayload payload) {
        return new RoutingRuleAction(payload.kind(), payload);
    }
}
