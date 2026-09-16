package com.mxraven.admin.model;

import java.util.List;

/**
 * Wire representation of a tenant domain and its DNS verification state.
 *
 * <p>Prefer the {@link Domain} entity, which exposes this data plus the
 * domain's operations.
 *
 * @param id domain identifier
 * @param tenantId owning tenant identifier
 * @param domainName domain name
 * @param verificationToken DNS ownership verification token
 * @param sendingEnabled whether outbound sending is enabled
 * @param dkimActiveSelector active DKIM selector
 * @param spfVerified whether SPF is verified
 * @param dkimVerified whether DKIM is verified
 * @param dmarcVerified whether DMARC is verified
 * @param dmarcReportAddress optional DMARC aggregate report mailbox
 * @param dnsLastCheckedAt timestamp of the last DNS check
 * @param status lifecycle status
 * @param requiredCustomerRecords DNS records the customer must create
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
