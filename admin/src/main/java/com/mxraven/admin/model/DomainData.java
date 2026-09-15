package com.mxraven.admin.model;

import java.util.List;

/**
 * Wire representation of a tenant domain and its DNS verification state.
 *
 * <p>Prefer the {@link Domain} entity, which exposes this data plus the
 * domain's operations.
 */
public record DomainData(
        String id,
        String tenantId,
        String domainName,
        String verificationToken,
        boolean sendingEnabled,
        String dkimActiveSelector,
        boolean spfVerified,
        boolean dkimVerified,
        boolean dmarcVerified,
        String dmarcReportAddress,
        String dnsLastCheckedAt,
        DomainStatus status,
        List<DnsInstructionRecord> requiredCustomerRecords) {
}
