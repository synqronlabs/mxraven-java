package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Wire representation backing the {@link TenantSuppression} entity.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class TenantSuppressionData {
    private final String tenantId;
    private final String emailAddress;
    private final SuppressionReason reason;

    /** identifier of the owning tenant */
    public String tenantId() {
        return tenantId;
    }

    /** suppressed email address */
    public String emailAddress() {
        return emailAddress;
    }

    /** reason the address is suppressed */
    public SuppressionReason reason() {
        return reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TenantSuppressionData that = (TenantSuppressionData) o;
        return Objects.equals(this.tenantId, that.tenantId)
                && Objects.equals(this.emailAddress, that.emailAddress)
                && Objects.equals(this.reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.tenantId, this.emailAddress, this.reason);
    }

    @Override
    public String toString() {
        return "TenantSuppressionData[" + "tenantId=" + this.tenantId + ", " + "emailAddress=" + this.emailAddress + ", " + "reason=" + this.reason + "]";
    }

    /**
     * Creates a new TenantSuppressionData.
     *
     * @param tenantId identifier of the owning tenant
     * @param emailAddress suppressed email address
     * @param reason reason the address is suppressed
     */
    @JsonCreator
    public TenantSuppressionData(String tenantId, String emailAddress, SuppressionReason reason) {
        this.tenantId = tenantId;
        this.emailAddress = emailAddress;
        this.reason = reason;
    }
}
