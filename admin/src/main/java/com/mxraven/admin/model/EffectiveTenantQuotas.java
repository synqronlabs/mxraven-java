package com.mxraven.admin.model;

/**
 * Effective tenant quota values after merging the guardrail profile with any
 * tenant overrides, plus the profile they were derived from.
 *
 * @param profileId guardrail profile identifier the quotas were derived from
 * @param profileRef guardrail profile reference
 * @param profileDisplayName guardrail profile display name
 * @param profileIsActive whether the guardrail profile is active
 * @param maxSubmissionListeners maximum submission listeners allowed
 * @param maxMtaListeners maximum MTA listeners allowed
 * @param maxDomains maximum domains allowed
 * @param maxInboundRoutes maximum inbound routes allowed
 * @param maxApiKeys maximum API keys allowed
 * @param maxApiKeysPerListener maximum API keys per listener
 * @param maxRoutingRules maximum routing rules allowed
 * @param maxRoutingRulesPerListener maximum routing rules per listener
 * @param maxSmtpRelays maximum SMTP relays allowed
 * @param maxStorageIntegrations maximum storage integrations allowed
 * @param maxWebhookEndpoints maximum webhook endpoints allowed
 * @param maxAutoReplyTemplates maximum auto-reply templates allowed
 * @param maxIdentityProviders maximum identity providers allowed
 * @param maxManualTenantSuppressions maximum manual tenant suppressions allowed
 * @param maxRecipientSets maximum recipient sets allowed
 * @param maxRecipientSetMembers maximum recipient-set members allowed
 * @param maxRecipientSetMembersPerSet maximum recipient-set members per set
 * @param maxDedicatedIpLeases maximum dedicated IP leases allowed
 * @param maxDedicatedIpAddresses maximum dedicated IP addresses allowed
 * @param submissionBaseRecipientRatePerMinute base submission recipient rate per minute
 * @param submissionBurst submission burst allowance
 * @param submissionMaxConcurrency maximum concurrent submission tasks
 * @param submissionMinRateMultiplier minimum submission rate multiplier
 * @param mtaMessageRatePerMinute MTA message rate per minute
 * @param mtaRecipientRatePerMinute MTA recipient rate per minute
 * @param mtaTaskRatePerMinute MTA task rate per minute
 * @param mtaBurst MTA burst allowance
 * @param mtaMaxConcurrency maximum concurrent MTA tasks
 * @param mtaRspamdScanningEnabled whether Rspamd scanning is enabled for MTA listeners
 * @param reputationRecoveryTargetScore target reputation score for recovery
 * @param reputationSpamComplaintWeight reputation weight applied to spam complaints
 * @param reputationPolicyViolationWeight reputation weight applied to policy violations
 * @param reputationHardBounceWeight reputation weight applied to hard bounces
 * @param reputationUnsubscribeWeight reputation weight applied to unsubscribes
 * @param reputationNegativeStreakBonusCap cap on the negative-streak bonus
 * @param reputationUnsubscribeWindowSeconds unsubscribe window in seconds
 * @param reputationUnsubscribeThreshold unsubscribe count threshold
 * @param reputationRecoveryQuietPeriodSeconds quiet period before recovery in seconds
 * @param reputationRecoverySuccessesPerPoint successful sends required per recovery point
 * @param abuseControlEnabled whether abuse control is enabled
 * @param abuseAutoPauseEnabled whether automatic pausing on abuse is enabled
 * @param abuseEvaluationWindowSeconds abuse evaluation window in seconds
 * @param abuseMinEgressTasks minimum egress tasks before abuse evaluation
 * @param abuseSuppressedSendRateThreshold suppressed-send rate abuse threshold
 * @param abuseSuppressionAppliedRateThreshold suppression-applied rate abuse threshold
 * @param abuseSpamComplaintRateThreshold spam-complaint rate abuse threshold
 * @param abuseHardBounceRateThreshold hard-bounce rate abuse threshold
 * @param abusePolicyViolationRateThreshold policy-violation rate abuse threshold
 * @param abuseUnsubscribeRateThreshold unsubscribe rate abuse threshold
 * @param abuseLowReputationScoreThreshold low reputation score abuse threshold
 */
public record EffectiveTenantQuotas(
        String profileId,
        String profileRef,
        String profileDisplayName,
        boolean profileIsActive,
        Integer maxSubmissionListeners,
        Integer maxMtaListeners,
        Integer maxDomains,
        Integer maxInboundRoutes,
        Integer maxApiKeys,
        Integer maxApiKeysPerListener,
        Integer maxRoutingRules,
        Integer maxRoutingRulesPerListener,
        Integer maxSmtpRelays,
        Integer maxStorageIntegrations,
        Integer maxWebhookEndpoints,
        Integer maxAutoReplyTemplates,
        Integer maxIdentityProviders,
        Integer maxManualTenantSuppressions,
        Integer maxRecipientSets,
        Integer maxRecipientSetMembers,
        Integer maxRecipientSetMembersPerSet,
        Integer maxDedicatedIpLeases,
        Integer maxDedicatedIpAddresses,
        Double submissionBaseRecipientRatePerMinute,
        Integer submissionBurst,
        Integer submissionMaxConcurrency,
        Double submissionMinRateMultiplier,
        Double mtaMessageRatePerMinute,
        Double mtaRecipientRatePerMinute,
        Double mtaTaskRatePerMinute,
        Integer mtaBurst,
        Integer mtaMaxConcurrency,
        Boolean mtaRspamdScanningEnabled,
        Integer reputationRecoveryTargetScore,
        Integer reputationSpamComplaintWeight,
        Integer reputationPolicyViolationWeight,
        Integer reputationHardBounceWeight,
        Integer reputationUnsubscribeWeight,
        Integer reputationNegativeStreakBonusCap,
        Long reputationUnsubscribeWindowSeconds,
        Long reputationUnsubscribeThreshold,
        Long reputationRecoveryQuietPeriodSeconds,
        Long reputationRecoverySuccessesPerPoint,
        Boolean abuseControlEnabled,
        Boolean abuseAutoPauseEnabled,
        Long abuseEvaluationWindowSeconds,
        Long abuseMinEgressTasks,
        Double abuseSuppressedSendRateThreshold,
        Double abuseSuppressionAppliedRateThreshold,
        Double abuseSpamComplaintRateThreshold,
        Double abuseHardBounceRateThreshold,
        Double abusePolicyViolationRateThreshold,
        Double abuseUnsubscribeRateThreshold,
        Integer abuseLowReputationScoreThreshold) {
}
