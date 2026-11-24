package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Wire representation backing the {@link TenantSuppression} entity.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class TenantSuppressionData {
    private final String tenantId;
    private final String emailAddress;
    private final String canonicalEmailAddress;
    private final SuppressionReason reason;
    private final boolean systemManaged;
    private final String source;
    private final long feedbackCount;
    private final String addedAt;
    private final String lastFeedbackAt;
    private final String updatedAt;

    /** identifier of the owning tenant */
    public String tenantId() {
        return tenantId;
    }

    /** suppressed email address */
    public String emailAddress() {
        return emailAddress;
    }

    /** provider-canonical mailbox identity used for matching */
    public String canonicalEmailAddress() {
        return canonicalEmailAddress;
    }

    /** reason the address is suppressed */
    public SuppressionReason reason() {
        return reason;
    }

    /** whether mxRaven feedback, rather than a customer, wrote it */
    public boolean systemManaged() {
        return systemManaged;
    }

    /** opaque provenance label such as {@code one_click_unsubscribe} */
    public String source() {
        return source;
    }

    /** number of feedback signals that contributed to the suppression */
    public long feedbackCount() {
        return feedbackCount;
    }

    /** timestamp when the suppression was added */
    public String addedAt() {
        return addedAt;
    }

    /** timestamp of the latest contributing feedback signal */
    public String lastFeedbackAt() {
        return lastFeedbackAt;
    }

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
        TenantSuppressionData that = (TenantSuppressionData) o;
        return Objects.equals(this.tenantId, that.tenantId) && Objects.equals(this.emailAddress, that.emailAddress) && Objects.equals(this.canonicalEmailAddress, that.canonicalEmailAddress) && Objects.equals(this.reason, that.reason) && this.systemManaged == that.systemManaged && Objects.equals(this.source, that.source) && this.feedbackCount == that.feedbackCount && Objects.equals(this.addedAt, that.addedAt) && Objects.equals(this.lastFeedbackAt, that.lastFeedbackAt) && Objects.equals(this.updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.tenantId, this.emailAddress, this.canonicalEmailAddress, this.reason, this.systemManaged, this.source, this.feedbackCount, this.addedAt, this.lastFeedbackAt, this.updatedAt);
    }

    @Override
    public String toString() {
        return "TenantSuppressionData[" + "tenantId=" + this.tenantId + ", " + "emailAddress=" + this.emailAddress + ", " + "canonicalEmailAddress=" + this.canonicalEmailAddress + ", " + "reason=" + this.reason + ", " + "systemManaged=" + this.systemManaged + ", " + "source=" + this.source + ", " + "feedbackCount=" + this.feedbackCount + ", " + "addedAt=" + this.addedAt + ", " + "lastFeedbackAt=" + this.lastFeedbackAt + ", " + "updatedAt=" + this.updatedAt + "]";
    }

    /**
     * Creates a new TenantSuppressionData.
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
     * @param updatedAt updatedAt
     */
    @JsonCreator
    public TenantSuppressionData(String tenantId, String emailAddress, String canonicalEmailAddress, SuppressionReason reason, boolean systemManaged, String source, long feedbackCount, String addedAt, String lastFeedbackAt, String updatedAt) {
        this.tenantId = tenantId;
        this.emailAddress = emailAddress;
        this.canonicalEmailAddress = canonicalEmailAddress;
        this.reason = reason;
        this.systemManaged = systemManaged;
        this.source = source;
        this.feedbackCount = feedbackCount;
        this.addedAt = addedAt;
        this.lastFeedbackAt = lastFeedbackAt;
        this.updatedAt = updatedAt;
    }
}
