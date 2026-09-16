package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Wire representation backing the {@link SmtpForwardDestination} entity.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class SmtpForwardDestinationData {
    private final String id;
    private final String tenantId;
    private final String destinationRef;
    private final String displayName;
    private final String emailAddress;
    private final SmtpForwardDestinationVerificationStatus verificationStatus;
    private final SmtpForwardDestinationVerificationMethod verificationMethod;
    private final SmtpForwardVerificationDeliveryStatus deliveryStatus;
    private final String verifiedAt;
    private final String deliveryRequestedAt;
    private final String verificationEmailSentAt;
    private final String createdAt;
    private final String updatedAt;

    /** unique destination identifier */
    public String id() {
        return id;
    }

    /** identifier of the owning tenant */
    public String tenantId() {
        return tenantId;
    }

    /** stable reference for the destination */
    public String destinationRef() {
        return destinationRef;
    }

    /** human-readable destination name */
    public String displayName() {
        return displayName;
    }

    /** destination email address */
    public String emailAddress() {
        return emailAddress;
    }

    /** ownership verification status */
    public SmtpForwardDestinationVerificationStatus verificationStatus() {
        return verificationStatus;
    }

    /** how ownership is verified */
    public SmtpForwardDestinationVerificationMethod verificationMethod() {
        return verificationMethod;
    }

    /** status of the verification email delivery */
    public SmtpForwardVerificationDeliveryStatus deliveryStatus() {
        return deliveryStatus;
    }

    /** timestamp when ownership was verified */
    public String verifiedAt() {
        return verifiedAt;
    }

    /** timestamp when verification delivery was requested */
    public String deliveryRequestedAt() {
        return deliveryRequestedAt;
    }

    /** timestamp when the verification email was last sent */
    public String verificationEmailSentAt() {
        return verificationEmailSentAt;
    }

    /** timestamp when the destination was created */
    public String createdAt() {
        return createdAt;
    }

    /** timestamp when the destination was last updated */
    public String updatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SmtpForwardDestinationData that = (SmtpForwardDestinationData) o;
        return Objects.equals(this.id, that.id)
                && Objects.equals(this.tenantId, that.tenantId)
                && Objects.equals(this.destinationRef, that.destinationRef)
                && Objects.equals(this.displayName, that.displayName)
                && Objects.equals(this.emailAddress, that.emailAddress)
                && Objects.equals(this.verificationStatus, that.verificationStatus)
                && Objects.equals(this.verificationMethod, that.verificationMethod)
                && Objects.equals(this.deliveryStatus, that.deliveryStatus)
                && Objects.equals(this.verifiedAt, that.verifiedAt)
                && Objects.equals(this.deliveryRequestedAt, that.deliveryRequestedAt)
                && Objects.equals(this.verificationEmailSentAt, that.verificationEmailSentAt)
                && Objects.equals(this.createdAt, that.createdAt)
                && Objects.equals(this.updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.tenantId, this.destinationRef, this.displayName, this.emailAddress, this.verificationStatus, this.verificationMethod, this.deliveryStatus, this.verifiedAt, this.deliveryRequestedAt, this.verificationEmailSentAt, this.createdAt, this.updatedAt);
    }

    @Override
    public String toString() {
        return "SmtpForwardDestinationData[" + "id=" + this.id + ", " + "tenantId=" + this.tenantId + ", " + "destinationRef=" + this.destinationRef + ", " + "displayName=" + this.displayName + ", " + "emailAddress=" + this.emailAddress + ", " + "verificationStatus=" + this.verificationStatus + ", " + "verificationMethod=" + this.verificationMethod + ", " + "deliveryStatus=" + this.deliveryStatus + ", " + "verifiedAt=" + this.verifiedAt + ", " + "deliveryRequestedAt=" + this.deliveryRequestedAt + ", " + "verificationEmailSentAt=" + this.verificationEmailSentAt + ", " + "createdAt=" + this.createdAt + ", " + "updatedAt=" + this.updatedAt + "]";
    }

    /**
     * Creates a new SmtpForwardDestinationData.
     *
     * @param id unique destination identifier
     * @param tenantId identifier of the owning tenant
     * @param destinationRef stable reference for the destination
     * @param displayName human-readable destination name
     * @param emailAddress destination email address
     * @param verificationStatus ownership verification status
     * @param verificationMethod how ownership is verified
     * @param deliveryStatus status of the verification email delivery
     * @param verifiedAt timestamp when ownership was verified
     * @param deliveryRequestedAt timestamp when verification delivery was requested
     * @param verificationEmailSentAt timestamp when the verification email was last sent
     * @param createdAt timestamp when the destination was created
     * @param updatedAt timestamp when the destination was last updated
     */
    @JsonCreator
    public SmtpForwardDestinationData(String id, String tenantId, String destinationRef, String displayName, String emailAddress, SmtpForwardDestinationVerificationStatus verificationStatus, SmtpForwardDestinationVerificationMethod verificationMethod, SmtpForwardVerificationDeliveryStatus deliveryStatus, String verifiedAt, String deliveryRequestedAt, String verificationEmailSentAt, String createdAt, String updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.destinationRef = destinationRef;
        this.displayName = displayName;
        this.emailAddress = emailAddress;
        this.verificationStatus = verificationStatus;
        this.verificationMethod = verificationMethod;
        this.deliveryStatus = deliveryStatus;
        this.verifiedAt = verifiedAt;
        this.deliveryRequestedAt = deliveryRequestedAt;
        this.verificationEmailSentAt = verificationEmailSentAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
