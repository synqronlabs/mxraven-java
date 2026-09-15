package com.mxraven.admin.model;

import java.util.List;

/** Wire representation backing the {@link InboundRoute} entity. */
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
