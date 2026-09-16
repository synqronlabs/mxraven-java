package com.mxraven.admin.model;

import java.util.List;

/**
 * Wire representation backing the {@link InboundRoute} entity.
 *
 * @param id unique inbound-route identifier
 * @param tenantId identifier of the owning tenant
 * @param mtaListenerId identifier of the MTA listener that receives routed mail
 * @param domainName inbound domain name
 * @param isVerified whether the route is fully verified
 * @param verificationStatus current DNS verification status
 * @param txtVerified whether the TXT ownership record is verified
 * @param mxVerified whether the MX record is verified
 * @param dnsLastCheckedAt timestamp of the last DNS check
 * @param verificationToken token expected in the TXT ownership record
 * @param requiredCustomerRecords DNS records the customer must create
 */
public record InboundRouteData(
        String id,
        String tenantId,
        String mtaListenerId,
        String domainName,
        boolean isVerified,
        InboundRouteVerificationStatus verificationStatus,
        boolean txtVerified,
        boolean mxVerified,
        String dnsLastCheckedAt,
        String verificationToken,
        List<DnsInstructionRecord> requiredCustomerRecords) {
}
