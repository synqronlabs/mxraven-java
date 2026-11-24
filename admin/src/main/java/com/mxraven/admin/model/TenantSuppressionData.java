package com.mxraven.admin.model;

/**
 * Wire representation backing the {@link TenantSuppression} entity.
 *
 * @param tenantId identifier of the owning tenant
 * @param emailAddress suppressed email address
 * @param canonicalEmailAddress provider-canonical mailbox identity used for matching
 * @param reason reason the address is suppressed
 * @param systemManaged whether mxRaven feedback, rather than a customer, wrote it
 * @param source opaque provenance label such as {@code one_click_unsubscribe}
 * @param feedbackCount number of feedback signals that contributed to the suppression
 * @param addedAt timestamp when the suppression was added
 * @param lastFeedbackAt timestamp of the latest contributing feedback signal
 * @param updatedAt timestamp when the suppression was last updated
 */
public record TenantSuppressionData(
        String tenantId,
        String emailAddress,
        String canonicalEmailAddress,
        SuppressionReason reason,
        boolean systemManaged,
        String source,
        long feedbackCount,
        String addedAt,
        String lastFeedbackAt,
        String updatedAt) {
}
