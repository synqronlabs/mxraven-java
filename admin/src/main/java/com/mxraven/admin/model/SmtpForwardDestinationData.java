package com.mxraven.admin.model;

/** Wire representation backing the {@link SmtpForwardDestination} entity. */
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
