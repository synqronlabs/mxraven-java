package com.mxraven.admin.model;

/**
 * Wire representation backing the {@link SmtpForwardDestination} entity.
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
public record SmtpForwardDestinationData(
        String id,
        String tenantId,
        String destinationRef,
        String displayName,
        String emailAddress,
        SmtpForwardDestinationVerificationStatus verificationStatus,
        SmtpForwardDestinationVerificationMethod verificationMethod,
        SmtpForwardVerificationDeliveryStatus deliveryStatus,
        String verifiedAt,
        String deliveryRequestedAt,
        String verificationEmailSentAt,
        String createdAt,
        String updatedAt) {
}
