package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * Wire representation backing the {@link InboundRoute} entity.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class InboundRouteData {
    private final String id;
    private final String tenantId;
    private final String mtaListenerId;
    private final String domainName;
    private final boolean isVerified;
    private final InboundRouteVerificationStatus verificationStatus;
    private final boolean txtVerified;
    private final boolean mxVerified;
    private final String dnsLastCheckedAt;
    private final String verificationToken;
    private final List<DnsInstructionRecord> requiredCustomerRecords;

    /** unique inbound-route identifier */
    public String id() {
        return id;
    }

    /** identifier of the owning tenant */
    public String tenantId() {
        return tenantId;
    }

    /** identifier of the MTA listener that receives routed mail */
    public String mtaListenerId() {
        return mtaListenerId;
    }

    /** inbound domain name */
    public String domainName() {
        return domainName;
    }

    /** whether the route is fully verified */
    public boolean isVerified() {
        return isVerified;
    }

    /** current DNS verification status */
    public InboundRouteVerificationStatus verificationStatus() {
        return verificationStatus;
    }

    /** whether the TXT ownership record is verified */
    public boolean txtVerified() {
        return txtVerified;
    }

    /** whether the MX record is verified */
    public boolean mxVerified() {
        return mxVerified;
    }

    /** timestamp of the last DNS check */
    public String dnsLastCheckedAt() {
        return dnsLastCheckedAt;
    }

    /** token expected in the TXT ownership record */
    public String verificationToken() {
        return verificationToken;
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
        InboundRouteData that = (InboundRouteData) o;
        return Objects.equals(this.id, that.id)
                && Objects.equals(this.tenantId, that.tenantId)
                && Objects.equals(this.mtaListenerId, that.mtaListenerId)
                && Objects.equals(this.domainName, that.domainName)
                && this.isVerified == that.isVerified
                && Objects.equals(this.verificationStatus, that.verificationStatus)
                && this.txtVerified == that.txtVerified
                && this.mxVerified == that.mxVerified
                && Objects.equals(this.dnsLastCheckedAt, that.dnsLastCheckedAt)
                && Objects.equals(this.verificationToken, that.verificationToken)
                && Objects.equals(this.requiredCustomerRecords, that.requiredCustomerRecords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.tenantId, this.mtaListenerId, this.domainName, this.isVerified, this.verificationStatus, this.txtVerified, this.mxVerified, this.dnsLastCheckedAt, this.verificationToken, this.requiredCustomerRecords);
    }

    @Override
    public String toString() {
        return "InboundRouteData[" + "id=" + this.id + ", " + "tenantId=" + this.tenantId + ", " + "mtaListenerId=" + this.mtaListenerId + ", " + "domainName=" + this.domainName + ", " + "isVerified=" + this.isVerified + ", " + "verificationStatus=" + this.verificationStatus + ", " + "txtVerified=" + this.txtVerified + ", " + "mxVerified=" + this.mxVerified + ", " + "dnsLastCheckedAt=" + this.dnsLastCheckedAt + ", " + "verificationToken=" + this.verificationToken + ", " + "requiredCustomerRecords=" + this.requiredCustomerRecords + "]";
    }

    /**
     * Creates a new InboundRouteData.
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
    @JsonCreator
    public InboundRouteData(String id, String tenantId, String mtaListenerId, String domainName, boolean isVerified, InboundRouteVerificationStatus verificationStatus, boolean txtVerified, boolean mxVerified, String dnsLastCheckedAt, String verificationToken, List<DnsInstructionRecord> requiredCustomerRecords) {
        this.id = id;
        this.tenantId = tenantId;
        this.mtaListenerId = mtaListenerId;
        this.domainName = domainName;
        this.isVerified = isVerified;
        this.verificationStatus = verificationStatus;
        this.txtVerified = txtVerified;
        this.mxVerified = mxVerified;
        this.dnsLastCheckedAt = dnsLastCheckedAt;
        this.verificationToken = verificationToken;
        this.requiredCustomerRecords = requiredCustomerRecords;
    }
}
