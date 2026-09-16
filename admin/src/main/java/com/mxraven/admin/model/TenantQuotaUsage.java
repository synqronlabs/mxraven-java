package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Current resource usage for a tenant.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class TenantQuotaUsage {
    private final long submissionListeners;
    private final long mtaListeners;
    private final long domains;
    private final long inboundRoutes;
    private final long apiKeys;
    private final long routingRules;
    private final long smtpRelays;
    private final long storageIntegrations;
    private final long webhookEndpoints;
    private final long autoReplyTemplates;
    private final long identityProviders;
    private final long manualTenantSuppressions;
    private final long recipientSets;
    private final long recipientSetMembers;
    private final long dedicatedIpLeases;
    private final long dedicatedIpAddresses;

    /** number of submission listeners */
    public long submissionListeners() {
        return submissionListeners;
    }

    /** number of MTA listeners */
    public long mtaListeners() {
        return mtaListeners;
    }

    /** number of domains */
    public long domains() {
        return domains;
    }

    /** number of inbound routes */
    public long inboundRoutes() {
        return inboundRoutes;
    }

    /** number of API keys */
    public long apiKeys() {
        return apiKeys;
    }

    /** number of routing rules */
    public long routingRules() {
        return routingRules;
    }

    /** number of SMTP relays */
    public long smtpRelays() {
        return smtpRelays;
    }

    /** number of storage integrations */
    public long storageIntegrations() {
        return storageIntegrations;
    }

    /** number of webhook endpoints */
    public long webhookEndpoints() {
        return webhookEndpoints;
    }

    /** number of auto-reply templates */
    public long autoReplyTemplates() {
        return autoReplyTemplates;
    }

    /** number of identity providers */
    public long identityProviders() {
        return identityProviders;
    }

    /** number of manual tenant suppressions */
    public long manualTenantSuppressions() {
        return manualTenantSuppressions;
    }

    /** number of recipient sets */
    public long recipientSets() {
        return recipientSets;
    }

    /** number of recipient set members */
    public long recipientSetMembers() {
        return recipientSetMembers;
    }

    /** number of dedicated IP leases */
    public long dedicatedIpLeases() {
        return dedicatedIpLeases;
    }

    /** number of dedicated IP addresses */
    public long dedicatedIpAddresses() {
        return dedicatedIpAddresses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TenantQuotaUsage that = (TenantQuotaUsage) o;
        return this.submissionListeners == that.submissionListeners
                && this.mtaListeners == that.mtaListeners
                && this.domains == that.domains
                && this.inboundRoutes == that.inboundRoutes
                && this.apiKeys == that.apiKeys
                && this.routingRules == that.routingRules
                && this.smtpRelays == that.smtpRelays
                && this.storageIntegrations == that.storageIntegrations
                && this.webhookEndpoints == that.webhookEndpoints
                && this.autoReplyTemplates == that.autoReplyTemplates
                && this.identityProviders == that.identityProviders
                && this.manualTenantSuppressions == that.manualTenantSuppressions
                && this.recipientSets == that.recipientSets
                && this.recipientSetMembers == that.recipientSetMembers
                && this.dedicatedIpLeases == that.dedicatedIpLeases
                && this.dedicatedIpAddresses == that.dedicatedIpAddresses;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.submissionListeners, this.mtaListeners, this.domains, this.inboundRoutes, this.apiKeys, this.routingRules, this.smtpRelays, this.storageIntegrations, this.webhookEndpoints, this.autoReplyTemplates, this.identityProviders, this.manualTenantSuppressions, this.recipientSets, this.recipientSetMembers, this.dedicatedIpLeases, this.dedicatedIpAddresses);
    }

    @Override
    public String toString() {
        return "TenantQuotaUsage[" + "submissionListeners=" + this.submissionListeners + ", " + "mtaListeners=" + this.mtaListeners + ", " + "domains=" + this.domains + ", " + "inboundRoutes=" + this.inboundRoutes + ", " + "apiKeys=" + this.apiKeys + ", " + "routingRules=" + this.routingRules + ", " + "smtpRelays=" + this.smtpRelays + ", " + "storageIntegrations=" + this.storageIntegrations + ", " + "webhookEndpoints=" + this.webhookEndpoints + ", " + "autoReplyTemplates=" + this.autoReplyTemplates + ", " + "identityProviders=" + this.identityProviders + ", " + "manualTenantSuppressions=" + this.manualTenantSuppressions + ", " + "recipientSets=" + this.recipientSets + ", " + "recipientSetMembers=" + this.recipientSetMembers + ", " + "dedicatedIpLeases=" + this.dedicatedIpLeases + ", " + "dedicatedIpAddresses=" + this.dedicatedIpAddresses + "]";
    }

    /**
     * Creates a new TenantQuotaUsage.
     *
     * @param submissionListeners number of submission listeners
     * @param mtaListeners number of MTA listeners
     * @param domains number of domains
     * @param inboundRoutes number of inbound routes
     * @param apiKeys number of API keys
     * @param routingRules number of routing rules
     * @param smtpRelays number of SMTP relays
     * @param storageIntegrations number of storage integrations
     * @param webhookEndpoints number of webhook endpoints
     * @param autoReplyTemplates number of auto-reply templates
     * @param identityProviders number of identity providers
     * @param manualTenantSuppressions number of manual tenant suppressions
     * @param recipientSets number of recipient sets
     * @param recipientSetMembers number of recipient set members
     * @param dedicatedIpLeases number of dedicated IP leases
     * @param dedicatedIpAddresses number of dedicated IP addresses
     */
    @JsonCreator
    public TenantQuotaUsage(long submissionListeners, long mtaListeners, long domains, long inboundRoutes, long apiKeys, long routingRules, long smtpRelays, long storageIntegrations, long webhookEndpoints, long autoReplyTemplates, long identityProviders, long manualTenantSuppressions, long recipientSets, long recipientSetMembers, long dedicatedIpLeases, long dedicatedIpAddresses) {
        this.submissionListeners = submissionListeners;
        this.mtaListeners = mtaListeners;
        this.domains = domains;
        this.inboundRoutes = inboundRoutes;
        this.apiKeys = apiKeys;
        this.routingRules = routingRules;
        this.smtpRelays = smtpRelays;
        this.storageIntegrations = storageIntegrations;
        this.webhookEndpoints = webhookEndpoints;
        this.autoReplyTemplates = autoReplyTemplates;
        this.identityProviders = identityProviders;
        this.manualTenantSuppressions = manualTenantSuppressions;
        this.recipientSets = recipientSets;
        this.recipientSetMembers = recipientSetMembers;
        this.dedicatedIpLeases = dedicatedIpLeases;
        this.dedicatedIpAddresses = dedicatedIpAddresses;
    }
}
