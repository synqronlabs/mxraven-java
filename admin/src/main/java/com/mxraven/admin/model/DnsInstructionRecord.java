package com.mxraven.admin.model;

/**
 * One customer-facing DNS instruction required to onboard a domain.
 *
 * @param purpose  optional human-readable purpose
 * @param name     record host/name
 * @param type     one of {@code CNAME}, {@code TXT}, or {@code MX}
 * @param value    record value/target
 * @param priority optional MX priority
 * @param verified whether the record is currently verified
 */
public record DnsInstructionRecord(
        String purpose,
        String name,
        DnsRecordType type,
        String value,
        Integer priority,
        Boolean verified) {
}
