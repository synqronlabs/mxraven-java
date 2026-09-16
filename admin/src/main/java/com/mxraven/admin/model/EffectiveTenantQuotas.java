package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Effective tenant quota values after merging the guardrail profile with any
 * tenant overrides, plus the profile they were derived from.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class EffectiveTenantQuotas {
    private final String profileId;
    private final String profileRef;
    private final String profileDisplayName;
    private final boolean profileIsActive;
    private final Integer maxSubmissionListeners;
    private final Integer maxMtaListeners;
    private final Integer maxDomains;
    private final Integer maxInboundRoutes;
    private final Integer maxApiKeys;
    private final Integer maxApiKeysPerListener;
    private final Integer maxRoutingRules;
    private final Integer maxRoutingRulesPerListener;
    private final Integer maxSmtpRelays;
    private final Integer maxStorageIntegrations;
    private final Integer maxWebhookEndpoints;
    private final Integer maxAutoReplyTemplates;
    private final Integer maxIdentityProviders;
    private final Integer maxManualTenantSuppressions;
    private final Integer maxRecipientSets;
    private final Integer maxRecipientSetMembers;
    private final Integer maxRecipientSetMembersPerSet;
    private final Integer maxDedicatedIpLeases;
    private final Integer maxDedicatedIpAddresses;
    private final Double submissionBaseRecipientRatePerMinute;
    private final Integer submissionBurst;
    private final Integer submissionMaxConcurrency;
    private final Double submissionMinRateMultiplier;
    private final Double mtaMessageRatePerMinute;
    private final Double mtaRecipientRatePerMinute;
    private final Double mtaTaskRatePerMinute;
    private final Integer mtaBurst;
    private final Integer mtaMaxConcurrency;
    private final Boolean mtaRspamdScanningEnabled;
    private final Integer reputationRecoveryTargetScore;
    private final Integer reputationSpamComplaintWeight;
    private final Integer reputationPolicyViolationWeight;
    private final Integer reputationHardBounceWeight;
    private final Integer reputationUnsubscribeWeight;
    private final Integer reputationNegativeStreakBonusCap;
    private final Long reputationUnsubscribeWindowSeconds;
    private final Long reputationUnsubscribeThreshold;
    private final Long reputationRecoveryQuietPeriodSeconds;
    private final Long reputationRecoverySuccessesPerPoint;
    private final Boolean abuseControlEnabled;
    private final Boolean abuseAutoPauseEnabled;
    private final Long abuseEvaluationWindowSeconds;
    private final Long abuseMinEgressTasks;
    private final Double abuseSuppressedSendRateThreshold;
    private final Double abuseSuppressionAppliedRateThreshold;
    private final Double abuseSpamComplaintRateThreshold;
    private final Double abuseHardBounceRateThreshold;
    private final Double abusePolicyViolationRateThreshold;
    private final Double abuseUnsubscribeRateThreshold;
    private final Integer abuseLowReputationScoreThreshold;

    /** guardrail profile identifier the quotas were derived from */
    public String profileId() {
        return profileId;
    }

    /** guardrail profile reference */
    public String profileRef() {
        return profileRef;
    }

    /** guardrail profile display name */
    public String profileDisplayName() {
        return profileDisplayName;
    }

    /** whether the guardrail profile is active */
    public boolean profileIsActive() {
        return profileIsActive;
    }

    /** maximum submission listeners allowed */
    public Integer maxSubmissionListeners() {
        return maxSubmissionListeners;
    }

    /** maximum MTA listeners allowed */
    public Integer maxMtaListeners() {
        return maxMtaListeners;
    }

    /** maximum domains allowed */
    public Integer maxDomains() {
        return maxDomains;
    }

    /** maximum inbound routes allowed */
    public Integer maxInboundRoutes() {
        return maxInboundRoutes;
    }

    /** maximum API keys allowed */
    public Integer maxApiKeys() {
        return maxApiKeys;
    }

    /** maximum API keys per listener */
    public Integer maxApiKeysPerListener() {
        return maxApiKeysPerListener;
    }

    /** maximum routing rules allowed */
    public Integer maxRoutingRules() {
        return maxRoutingRules;
    }

    /** maximum routing rules per listener */
    public Integer maxRoutingRulesPerListener() {
        return maxRoutingRulesPerListener;
    }

    /** maximum SMTP relays allowed */
    public Integer maxSmtpRelays() {
        return maxSmtpRelays;
    }

    /** maximum storage integrations allowed */
    public Integer maxStorageIntegrations() {
        return maxStorageIntegrations;
    }

    /** maximum webhook endpoints allowed */
    public Integer maxWebhookEndpoints() {
        return maxWebhookEndpoints;
    }

    /** maximum auto-reply templates allowed */
    public Integer maxAutoReplyTemplates() {
        return maxAutoReplyTemplates;
    }

    /** maximum identity providers allowed */
    public Integer maxIdentityProviders() {
        return maxIdentityProviders;
    }

    /** maximum manual tenant suppressions allowed */
    public Integer maxManualTenantSuppressions() {
        return maxManualTenantSuppressions;
    }

    /** maximum recipient sets allowed */
    public Integer maxRecipientSets() {
        return maxRecipientSets;
    }

    /** maximum recipient-set members allowed */
    public Integer maxRecipientSetMembers() {
        return maxRecipientSetMembers;
    }

    /** maximum recipient-set members per set */
    public Integer maxRecipientSetMembersPerSet() {
        return maxRecipientSetMembersPerSet;
    }

    /** maximum dedicated IP leases allowed */
    public Integer maxDedicatedIpLeases() {
        return maxDedicatedIpLeases;
    }

    /** maximum dedicated IP addresses allowed */
    public Integer maxDedicatedIpAddresses() {
        return maxDedicatedIpAddresses;
    }

    /** base submission recipient rate per minute */
    public Double submissionBaseRecipientRatePerMinute() {
        return submissionBaseRecipientRatePerMinute;
    }

    /** submission burst allowance */
    public Integer submissionBurst() {
        return submissionBurst;
    }

    /** maximum concurrent submission tasks */
    public Integer submissionMaxConcurrency() {
        return submissionMaxConcurrency;
    }

    /** minimum submission rate multiplier */
    public Double submissionMinRateMultiplier() {
        return submissionMinRateMultiplier;
    }

    /** MTA message rate per minute */
    public Double mtaMessageRatePerMinute() {
        return mtaMessageRatePerMinute;
    }

    /** MTA recipient rate per minute */
    public Double mtaRecipientRatePerMinute() {
        return mtaRecipientRatePerMinute;
    }

    /** MTA task rate per minute */
    public Double mtaTaskRatePerMinute() {
        return mtaTaskRatePerMinute;
    }

    /** MTA burst allowance */
    public Integer mtaBurst() {
        return mtaBurst;
    }

    /** maximum concurrent MTA tasks */
    public Integer mtaMaxConcurrency() {
        return mtaMaxConcurrency;
    }

    /** whether Rspamd scanning is enabled for MTA listeners */
    public Boolean mtaRspamdScanningEnabled() {
        return mtaRspamdScanningEnabled;
    }

    /** target reputation score for recovery */
    public Integer reputationRecoveryTargetScore() {
        return reputationRecoveryTargetScore;
    }

    /** reputation weight applied to spam complaints */
    public Integer reputationSpamComplaintWeight() {
        return reputationSpamComplaintWeight;
    }

    /** reputation weight applied to policy violations */
    public Integer reputationPolicyViolationWeight() {
        return reputationPolicyViolationWeight;
    }

    /** reputation weight applied to hard bounces */
    public Integer reputationHardBounceWeight() {
        return reputationHardBounceWeight;
    }

    /** reputation weight applied to unsubscribes */
    public Integer reputationUnsubscribeWeight() {
        return reputationUnsubscribeWeight;
    }

    /** cap on the negative-streak bonus */
    public Integer reputationNegativeStreakBonusCap() {
        return reputationNegativeStreakBonusCap;
    }

    /** unsubscribe window in seconds */
    public Long reputationUnsubscribeWindowSeconds() {
        return reputationUnsubscribeWindowSeconds;
    }

    /** unsubscribe count threshold */
    public Long reputationUnsubscribeThreshold() {
        return reputationUnsubscribeThreshold;
    }

    /** quiet period before recovery in seconds */
    public Long reputationRecoveryQuietPeriodSeconds() {
        return reputationRecoveryQuietPeriodSeconds;
    }

    /** successful sends required per recovery point */
    public Long reputationRecoverySuccessesPerPoint() {
        return reputationRecoverySuccessesPerPoint;
    }

    /** whether abuse control is enabled */
    public Boolean abuseControlEnabled() {
        return abuseControlEnabled;
    }

    /** whether automatic pausing on abuse is enabled */
    public Boolean abuseAutoPauseEnabled() {
        return abuseAutoPauseEnabled;
    }

    /** abuse evaluation window in seconds */
    public Long abuseEvaluationWindowSeconds() {
        return abuseEvaluationWindowSeconds;
    }

    /** minimum egress tasks before abuse evaluation */
    public Long abuseMinEgressTasks() {
        return abuseMinEgressTasks;
    }

    /** suppressed-send rate abuse threshold */
    public Double abuseSuppressedSendRateThreshold() {
        return abuseSuppressedSendRateThreshold;
    }

    /** suppression-applied rate abuse threshold */
    public Double abuseSuppressionAppliedRateThreshold() {
        return abuseSuppressionAppliedRateThreshold;
    }

    /** spam-complaint rate abuse threshold */
    public Double abuseSpamComplaintRateThreshold() {
        return abuseSpamComplaintRateThreshold;
    }

    /** hard-bounce rate abuse threshold */
    public Double abuseHardBounceRateThreshold() {
        return abuseHardBounceRateThreshold;
    }

    /** policy-violation rate abuse threshold */
    public Double abusePolicyViolationRateThreshold() {
        return abusePolicyViolationRateThreshold;
    }

    /** unsubscribe rate abuse threshold */
    public Double abuseUnsubscribeRateThreshold() {
        return abuseUnsubscribeRateThreshold;
    }

    /** low reputation score abuse threshold */
    public Integer abuseLowReputationScoreThreshold() {
        return abuseLowReputationScoreThreshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EffectiveTenantQuotas that = (EffectiveTenantQuotas) o;
        return Objects.equals(this.profileId, that.profileId)
                && Objects.equals(this.profileRef, that.profileRef)
                && Objects.equals(this.profileDisplayName, that.profileDisplayName)
                && this.profileIsActive == that.profileIsActive
                && Objects.equals(this.maxSubmissionListeners, that.maxSubmissionListeners)
                && Objects.equals(this.maxMtaListeners, that.maxMtaListeners)
                && Objects.equals(this.maxDomains, that.maxDomains)
                && Objects.equals(this.maxInboundRoutes, that.maxInboundRoutes)
                && Objects.equals(this.maxApiKeys, that.maxApiKeys)
                && Objects.equals(this.maxApiKeysPerListener, that.maxApiKeysPerListener)
                && Objects.equals(this.maxRoutingRules, that.maxRoutingRules)
                && Objects.equals(this.maxRoutingRulesPerListener, that.maxRoutingRulesPerListener)
                && Objects.equals(this.maxSmtpRelays, that.maxSmtpRelays)
                && Objects.equals(this.maxStorageIntegrations, that.maxStorageIntegrations)
                && Objects.equals(this.maxWebhookEndpoints, that.maxWebhookEndpoints)
                && Objects.equals(this.maxAutoReplyTemplates, that.maxAutoReplyTemplates)
                && Objects.equals(this.maxIdentityProviders, that.maxIdentityProviders)
                && Objects.equals(this.maxManualTenantSuppressions, that.maxManualTenantSuppressions)
                && Objects.equals(this.maxRecipientSets, that.maxRecipientSets)
                && Objects.equals(this.maxRecipientSetMembers, that.maxRecipientSetMembers)
                && Objects.equals(this.maxRecipientSetMembersPerSet, that.maxRecipientSetMembersPerSet)
                && Objects.equals(this.maxDedicatedIpLeases, that.maxDedicatedIpLeases)
                && Objects.equals(this.maxDedicatedIpAddresses, that.maxDedicatedIpAddresses)
                && Objects.equals(this.submissionBaseRecipientRatePerMinute, that.submissionBaseRecipientRatePerMinute)
                && Objects.equals(this.submissionBurst, that.submissionBurst)
                && Objects.equals(this.submissionMaxConcurrency, that.submissionMaxConcurrency)
                && Objects.equals(this.submissionMinRateMultiplier, that.submissionMinRateMultiplier)
                && Objects.equals(this.mtaMessageRatePerMinute, that.mtaMessageRatePerMinute)
                && Objects.equals(this.mtaRecipientRatePerMinute, that.mtaRecipientRatePerMinute)
                && Objects.equals(this.mtaTaskRatePerMinute, that.mtaTaskRatePerMinute)
                && Objects.equals(this.mtaBurst, that.mtaBurst)
                && Objects.equals(this.mtaMaxConcurrency, that.mtaMaxConcurrency)
                && Objects.equals(this.mtaRspamdScanningEnabled, that.mtaRspamdScanningEnabled)
                && Objects.equals(this.reputationRecoveryTargetScore, that.reputationRecoveryTargetScore)
                && Objects.equals(this.reputationSpamComplaintWeight, that.reputationSpamComplaintWeight)
                && Objects.equals(this.reputationPolicyViolationWeight, that.reputationPolicyViolationWeight)
                && Objects.equals(this.reputationHardBounceWeight, that.reputationHardBounceWeight)
                && Objects.equals(this.reputationUnsubscribeWeight, that.reputationUnsubscribeWeight)
                && Objects.equals(this.reputationNegativeStreakBonusCap, that.reputationNegativeStreakBonusCap)
                && Objects.equals(this.reputationUnsubscribeWindowSeconds, that.reputationUnsubscribeWindowSeconds)
                && Objects.equals(this.reputationUnsubscribeThreshold, that.reputationUnsubscribeThreshold)
                && Objects.equals(this.reputationRecoveryQuietPeriodSeconds, that.reputationRecoveryQuietPeriodSeconds)
                && Objects.equals(this.reputationRecoverySuccessesPerPoint, that.reputationRecoverySuccessesPerPoint)
                && Objects.equals(this.abuseControlEnabled, that.abuseControlEnabled)
                && Objects.equals(this.abuseAutoPauseEnabled, that.abuseAutoPauseEnabled)
                && Objects.equals(this.abuseEvaluationWindowSeconds, that.abuseEvaluationWindowSeconds)
                && Objects.equals(this.abuseMinEgressTasks, that.abuseMinEgressTasks)
                && Objects.equals(this.abuseSuppressedSendRateThreshold, that.abuseSuppressedSendRateThreshold)
                && Objects.equals(this.abuseSuppressionAppliedRateThreshold, that.abuseSuppressionAppliedRateThreshold)
                && Objects.equals(this.abuseSpamComplaintRateThreshold, that.abuseSpamComplaintRateThreshold)
                && Objects.equals(this.abuseHardBounceRateThreshold, that.abuseHardBounceRateThreshold)
                && Objects.equals(this.abusePolicyViolationRateThreshold, that.abusePolicyViolationRateThreshold)
                && Objects.equals(this.abuseUnsubscribeRateThreshold, that.abuseUnsubscribeRateThreshold)
                && Objects.equals(this.abuseLowReputationScoreThreshold, that.abuseLowReputationScoreThreshold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.profileId, this.profileRef, this.profileDisplayName, this.profileIsActive, this.maxSubmissionListeners, this.maxMtaListeners, this.maxDomains, this.maxInboundRoutes, this.maxApiKeys, this.maxApiKeysPerListener, this.maxRoutingRules, this.maxRoutingRulesPerListener, this.maxSmtpRelays, this.maxStorageIntegrations, this.maxWebhookEndpoints, this.maxAutoReplyTemplates, this.maxIdentityProviders, this.maxManualTenantSuppressions, this.maxRecipientSets, this.maxRecipientSetMembers, this.maxRecipientSetMembersPerSet, this.maxDedicatedIpLeases, this.maxDedicatedIpAddresses, this.submissionBaseRecipientRatePerMinute, this.submissionBurst, this.submissionMaxConcurrency, this.submissionMinRateMultiplier, this.mtaMessageRatePerMinute, this.mtaRecipientRatePerMinute, this.mtaTaskRatePerMinute, this.mtaBurst, this.mtaMaxConcurrency, this.mtaRspamdScanningEnabled, this.reputationRecoveryTargetScore, this.reputationSpamComplaintWeight, this.reputationPolicyViolationWeight, this.reputationHardBounceWeight, this.reputationUnsubscribeWeight, this.reputationNegativeStreakBonusCap, this.reputationUnsubscribeWindowSeconds, this.reputationUnsubscribeThreshold, this.reputationRecoveryQuietPeriodSeconds, this.reputationRecoverySuccessesPerPoint, this.abuseControlEnabled, this.abuseAutoPauseEnabled, this.abuseEvaluationWindowSeconds, this.abuseMinEgressTasks, this.abuseSuppressedSendRateThreshold, this.abuseSuppressionAppliedRateThreshold, this.abuseSpamComplaintRateThreshold, this.abuseHardBounceRateThreshold, this.abusePolicyViolationRateThreshold, this.abuseUnsubscribeRateThreshold, this.abuseLowReputationScoreThreshold);
    }

    @Override
    public String toString() {
        return "EffectiveTenantQuotas[" + "profileId=" + this.profileId + ", " + "profileRef=" + this.profileRef + ", " + "profileDisplayName=" + this.profileDisplayName + ", " + "profileIsActive=" + this.profileIsActive + ", " + "maxSubmissionListeners=" + this.maxSubmissionListeners + ", " + "maxMtaListeners=" + this.maxMtaListeners + ", " + "maxDomains=" + this.maxDomains + ", " + "maxInboundRoutes=" + this.maxInboundRoutes + ", " + "maxApiKeys=" + this.maxApiKeys + ", " + "maxApiKeysPerListener=" + this.maxApiKeysPerListener + ", " + "maxRoutingRules=" + this.maxRoutingRules + ", " + "maxRoutingRulesPerListener=" + this.maxRoutingRulesPerListener + ", " + "maxSmtpRelays=" + this.maxSmtpRelays + ", " + "maxStorageIntegrations=" + this.maxStorageIntegrations + ", " + "maxWebhookEndpoints=" + this.maxWebhookEndpoints + ", " + "maxAutoReplyTemplates=" + this.maxAutoReplyTemplates + ", " + "maxIdentityProviders=" + this.maxIdentityProviders + ", " + "maxManualTenantSuppressions=" + this.maxManualTenantSuppressions + ", " + "maxRecipientSets=" + this.maxRecipientSets + ", " + "maxRecipientSetMembers=" + this.maxRecipientSetMembers + ", " + "maxRecipientSetMembersPerSet=" + this.maxRecipientSetMembersPerSet + ", " + "maxDedicatedIpLeases=" + this.maxDedicatedIpLeases + ", " + "maxDedicatedIpAddresses=" + this.maxDedicatedIpAddresses + ", " + "submissionBaseRecipientRatePerMinute=" + this.submissionBaseRecipientRatePerMinute + ", " + "submissionBurst=" + this.submissionBurst + ", " + "submissionMaxConcurrency=" + this.submissionMaxConcurrency + ", " + "submissionMinRateMultiplier=" + this.submissionMinRateMultiplier + ", " + "mtaMessageRatePerMinute=" + this.mtaMessageRatePerMinute + ", " + "mtaRecipientRatePerMinute=" + this.mtaRecipientRatePerMinute + ", " + "mtaTaskRatePerMinute=" + this.mtaTaskRatePerMinute + ", " + "mtaBurst=" + this.mtaBurst + ", " + "mtaMaxConcurrency=" + this.mtaMaxConcurrency + ", " + "mtaRspamdScanningEnabled=" + this.mtaRspamdScanningEnabled + ", " + "reputationRecoveryTargetScore=" + this.reputationRecoveryTargetScore + ", " + "reputationSpamComplaintWeight=" + this.reputationSpamComplaintWeight + ", " + "reputationPolicyViolationWeight=" + this.reputationPolicyViolationWeight + ", " + "reputationHardBounceWeight=" + this.reputationHardBounceWeight + ", " + "reputationUnsubscribeWeight=" + this.reputationUnsubscribeWeight + ", " + "reputationNegativeStreakBonusCap=" + this.reputationNegativeStreakBonusCap + ", " + "reputationUnsubscribeWindowSeconds=" + this.reputationUnsubscribeWindowSeconds + ", " + "reputationUnsubscribeThreshold=" + this.reputationUnsubscribeThreshold + ", " + "reputationRecoveryQuietPeriodSeconds=" + this.reputationRecoveryQuietPeriodSeconds + ", " + "reputationRecoverySuccessesPerPoint=" + this.reputationRecoverySuccessesPerPoint + ", " + "abuseControlEnabled=" + this.abuseControlEnabled + ", " + "abuseAutoPauseEnabled=" + this.abuseAutoPauseEnabled + ", " + "abuseEvaluationWindowSeconds=" + this.abuseEvaluationWindowSeconds + ", " + "abuseMinEgressTasks=" + this.abuseMinEgressTasks + ", " + "abuseSuppressedSendRateThreshold=" + this.abuseSuppressedSendRateThreshold + ", " + "abuseSuppressionAppliedRateThreshold=" + this.abuseSuppressionAppliedRateThreshold + ", " + "abuseSpamComplaintRateThreshold=" + this.abuseSpamComplaintRateThreshold + ", " + "abuseHardBounceRateThreshold=" + this.abuseHardBounceRateThreshold + ", " + "abusePolicyViolationRateThreshold=" + this.abusePolicyViolationRateThreshold + ", " + "abuseUnsubscribeRateThreshold=" + this.abuseUnsubscribeRateThreshold + ", " + "abuseLowReputationScoreThreshold=" + this.abuseLowReputationScoreThreshold + "]";
    }

    /**
     * Creates a new EffectiveTenantQuotas.
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
    @JsonCreator
    public EffectiveTenantQuotas(String profileId, String profileRef, String profileDisplayName, boolean profileIsActive, Integer maxSubmissionListeners, Integer maxMtaListeners, Integer maxDomains, Integer maxInboundRoutes, Integer maxApiKeys, Integer maxApiKeysPerListener, Integer maxRoutingRules, Integer maxRoutingRulesPerListener, Integer maxSmtpRelays, Integer maxStorageIntegrations, Integer maxWebhookEndpoints, Integer maxAutoReplyTemplates, Integer maxIdentityProviders, Integer maxManualTenantSuppressions, Integer maxRecipientSets, Integer maxRecipientSetMembers, Integer maxRecipientSetMembersPerSet, Integer maxDedicatedIpLeases, Integer maxDedicatedIpAddresses, Double submissionBaseRecipientRatePerMinute, Integer submissionBurst, Integer submissionMaxConcurrency, Double submissionMinRateMultiplier, Double mtaMessageRatePerMinute, Double mtaRecipientRatePerMinute, Double mtaTaskRatePerMinute, Integer mtaBurst, Integer mtaMaxConcurrency, Boolean mtaRspamdScanningEnabled, Integer reputationRecoveryTargetScore, Integer reputationSpamComplaintWeight, Integer reputationPolicyViolationWeight, Integer reputationHardBounceWeight, Integer reputationUnsubscribeWeight, Integer reputationNegativeStreakBonusCap, Long reputationUnsubscribeWindowSeconds, Long reputationUnsubscribeThreshold, Long reputationRecoveryQuietPeriodSeconds, Long reputationRecoverySuccessesPerPoint, Boolean abuseControlEnabled, Boolean abuseAutoPauseEnabled, Long abuseEvaluationWindowSeconds, Long abuseMinEgressTasks, Double abuseSuppressedSendRateThreshold, Double abuseSuppressionAppliedRateThreshold, Double abuseSpamComplaintRateThreshold, Double abuseHardBounceRateThreshold, Double abusePolicyViolationRateThreshold, Double abuseUnsubscribeRateThreshold, Integer abuseLowReputationScoreThreshold) {
        this.profileId = profileId;
        this.profileRef = profileRef;
        this.profileDisplayName = profileDisplayName;
        this.profileIsActive = profileIsActive;
        this.maxSubmissionListeners = maxSubmissionListeners;
        this.maxMtaListeners = maxMtaListeners;
        this.maxDomains = maxDomains;
        this.maxInboundRoutes = maxInboundRoutes;
        this.maxApiKeys = maxApiKeys;
        this.maxApiKeysPerListener = maxApiKeysPerListener;
        this.maxRoutingRules = maxRoutingRules;
        this.maxRoutingRulesPerListener = maxRoutingRulesPerListener;
        this.maxSmtpRelays = maxSmtpRelays;
        this.maxStorageIntegrations = maxStorageIntegrations;
        this.maxWebhookEndpoints = maxWebhookEndpoints;
        this.maxAutoReplyTemplates = maxAutoReplyTemplates;
        this.maxIdentityProviders = maxIdentityProviders;
        this.maxManualTenantSuppressions = maxManualTenantSuppressions;
        this.maxRecipientSets = maxRecipientSets;
        this.maxRecipientSetMembers = maxRecipientSetMembers;
        this.maxRecipientSetMembersPerSet = maxRecipientSetMembersPerSet;
        this.maxDedicatedIpLeases = maxDedicatedIpLeases;
        this.maxDedicatedIpAddresses = maxDedicatedIpAddresses;
        this.submissionBaseRecipientRatePerMinute = submissionBaseRecipientRatePerMinute;
        this.submissionBurst = submissionBurst;
        this.submissionMaxConcurrency = submissionMaxConcurrency;
        this.submissionMinRateMultiplier = submissionMinRateMultiplier;
        this.mtaMessageRatePerMinute = mtaMessageRatePerMinute;
        this.mtaRecipientRatePerMinute = mtaRecipientRatePerMinute;
        this.mtaTaskRatePerMinute = mtaTaskRatePerMinute;
        this.mtaBurst = mtaBurst;
        this.mtaMaxConcurrency = mtaMaxConcurrency;
        this.mtaRspamdScanningEnabled = mtaRspamdScanningEnabled;
        this.reputationRecoveryTargetScore = reputationRecoveryTargetScore;
        this.reputationSpamComplaintWeight = reputationSpamComplaintWeight;
        this.reputationPolicyViolationWeight = reputationPolicyViolationWeight;
        this.reputationHardBounceWeight = reputationHardBounceWeight;
        this.reputationUnsubscribeWeight = reputationUnsubscribeWeight;
        this.reputationNegativeStreakBonusCap = reputationNegativeStreakBonusCap;
        this.reputationUnsubscribeWindowSeconds = reputationUnsubscribeWindowSeconds;
        this.reputationUnsubscribeThreshold = reputationUnsubscribeThreshold;
        this.reputationRecoveryQuietPeriodSeconds = reputationRecoveryQuietPeriodSeconds;
        this.reputationRecoverySuccessesPerPoint = reputationRecoverySuccessesPerPoint;
        this.abuseControlEnabled = abuseControlEnabled;
        this.abuseAutoPauseEnabled = abuseAutoPauseEnabled;
        this.abuseEvaluationWindowSeconds = abuseEvaluationWindowSeconds;
        this.abuseMinEgressTasks = abuseMinEgressTasks;
        this.abuseSuppressedSendRateThreshold = abuseSuppressedSendRateThreshold;
        this.abuseSuppressionAppliedRateThreshold = abuseSuppressionAppliedRateThreshold;
        this.abuseSpamComplaintRateThreshold = abuseSpamComplaintRateThreshold;
        this.abuseHardBounceRateThreshold = abuseHardBounceRateThreshold;
        this.abusePolicyViolationRateThreshold = abusePolicyViolationRateThreshold;
        this.abuseUnsubscribeRateThreshold = abuseUnsubscribeRateThreshold;
        this.abuseLowReputationScoreThreshold = abuseLowReputationScoreThreshold;
    }
}
