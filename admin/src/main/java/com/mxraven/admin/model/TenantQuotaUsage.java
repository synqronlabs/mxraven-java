package com.mxraven.admin.model;

/**
 * Current resource usage for a tenant.
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
public record TenantQuotaUsage(
        long submissionListeners,
        long mtaListeners,
        long domains,
        long inboundRoutes,
        long apiKeys,
        long routingRules,
        long smtpRelays,
        long storageIntegrations,
        long webhookEndpoints,
        long autoReplyTemplates,
        long identityProviders,
        long manualTenantSuppressions,
        long recipientSets,
        long recipientSetMembers,
        long dedicatedIpLeases,
        long dedicatedIpAddresses) {
}
