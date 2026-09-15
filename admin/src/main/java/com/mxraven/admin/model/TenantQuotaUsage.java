package com.mxraven.admin.model;

/**
 * Current resource usage for a tenant.
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
