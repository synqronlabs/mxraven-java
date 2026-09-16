package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * Wire representation of a tenant domain and its DNS verification state.
 *
 * <p>Prefer the {@link Domain} entity, which exposes this data plus the
 * domain's operations.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class DomainData {
    private final String id;
    private final String tenantId;
    private final String domainName;
    private final String verificationToken;
    private final boolean sendingEnabled;
    private final String dkimActiveSelector;
    private final boolean spfVerified;
    private final boolean dkimVerified;
    private final boolean dmarcVerified;
    private final String dmarcReportAddress;
    private final String dnsLastCheckedAt;
    private final DomainStatus status;
    private final List<DnsInstructionRecord> requiredCustomerRecords;

    /** domain identifier */
    public String id() {
        return id;
    }

    /** owning tenant identifier */
    public String tenantId() {
        return tenantId;
    }

    /** domain name */
    public String domainName() {
        return domainName;
    }

    /** DNS ownership verification token */
    public String verificationToken() {
        return verificationToken;
    }

    /** whether outbound sending is enabled */
    public boolean sendingEnabled() {
        return sendingEnabled;
    }

    /** active DKIM selector */
    public String dkimActiveSelector() {
        return dkimActiveSelector;
    }

    /** whether SPF is verified */
    public boolean spfVerified() {
        return spfVerified;
    }

    /** whether DKIM is verified */
    public boolean dkimVerified() {
        return dkimVerified;
    }

    /** whether DMARC is verified */
    public boolean dmarcVerified() {
        return dmarcVerified;
    }

    /** optional DMARC aggregate report mailbox */
    public String dmarcReportAddress() {
        return dmarcReportAddress;
    }

    /** timestamp of the last DNS check */
    public String dnsLastCheckedAt() {
        return dnsLastCheckedAt;
    }

    /** lifecycle status */
    public DomainStatus status() {
        return status;
    }

    /** DNS records the customer must create */
    public List<DnsInstructionRecord> requiredCustomerRecords() {
        return requiredCustomerRecords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DomainData that = (DomainData) o;
        return Objects.equals(this.id, that.id)
                && Objects.equals(this.tenantId, that.tenantId)
                && Objects.equals(this.domainName, that.domainName)
                && Objects.equals(this.verificationToken, that.verificationToken)
                && this.sendingEnabled == that.sendingEnabled
                && Objects.equals(this.dkimActiveSelector, that.dkimActiveSelector)
                && this.spfVerified == that.spfVerified
                && this.dkimVerified == that.dkimVerified
                && this.dmarcVerified == that.dmarcVerified
                && Objects.equals(this.dmarcReportAddress, that.dmarcReportAddress)
                && Objects.equals(this.dnsLastCheckedAt, that.dnsLastCheckedAt)
                && Objects.equals(this.status, that.status)
                && Objects.equals(this.requiredCustomerRecords, that.requiredCustomerRecords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.tenantId, this.domainName, this.verificationToken, this.sendingEnabled, this.dkimActiveSelector, this.spfVerified, this.dkimVerified, this.dmarcVerified, this.dmarcReportAddress, this.dnsLastCheckedAt, this.status, this.requiredCustomerRecords);
    }

    @Override
    public String toString() {
        return "DomainData[" + "id=" + this.id + ", " + "tenantId=" + this.tenantId + ", " + "domainName=" + this.domainName + ", " + "verificationToken=" + this.verificationToken + ", " + "sendingEnabled=" + this.sendingEnabled + ", " + "dkimActiveSelector=" + this.dkimActiveSelector + ", " + "spfVerified=" + this.spfVerified + ", " + "dkimVerified=" + this.dkimVerified + ", " + "dmarcVerified=" + this.dmarcVerified + ", " + "dmarcReportAddress=" + this.dmarcReportAddress + ", " + "dnsLastCheckedAt=" + this.dnsLastCheckedAt + ", " + "status=" + this.status + ", " + "requiredCustomerRecords=" + this.requiredCustomerRecords + "]";
    }

    /**
     * Creates a new DomainData.
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
    @JsonCreator
    public DomainData(String id, String tenantId, String domainName, String verificationToken, boolean sendingEnabled, String dkimActiveSelector, boolean spfVerified, boolean dkimVerified, boolean dmarcVerified, String dmarcReportAddress, String dnsLastCheckedAt, DomainStatus status, List<DnsInstructionRecord> requiredCustomerRecords) {
        this.id = id;
        this.tenantId = tenantId;
        this.domainName = domainName;
        this.verificationToken = verificationToken;
        this.sendingEnabled = sendingEnabled;
        this.dkimActiveSelector = dkimActiveSelector;
        this.spfVerified = spfVerified;
        this.dkimVerified = dkimVerified;
        this.dmarcVerified = dmarcVerified;
        this.dmarcReportAddress = dmarcReportAddress;
        this.dnsLastCheckedAt = dnsLastCheckedAt;
        this.status = status;
        this.requiredCustomerRecords = requiredCustomerRecords;
    }
}
