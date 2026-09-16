package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Wire representation backing the {@link AutoReplyTemplate} entity.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class AutoReplyTemplateData {
    private final String id;
    private final String tenantId;
    private final String templateRef;
    private final String displayName;
    private final String fromAddress;
    private final boolean isActive;
    private final AutoReplyTemplateContentSummary content;
    private final AutoReplySenderReadiness senderReadiness;

    /** template identifier */
    public String id() {
        return id;
    }

    /** owning tenant identifier */
    public String tenantId() {
        return tenantId;
    }

    /** immutable template reference */
    public String templateRef() {
        return templateRef;
    }

    /** human-readable template name */
    public String displayName() {
        return displayName;
    }

    /** sender address used for replies */
    public String fromAddress() {
        return fromAddress;
    }

    /** whether the template is active */
    public boolean isActive() {
        return isActive;
    }

    /** masked content summary */
    public AutoReplyTemplateContentSummary content() {
        return content;
    }

    /** sender readiness for the template */
    public AutoReplySenderReadiness senderReadiness() {
        return senderReadiness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AutoReplyTemplateData that = (AutoReplyTemplateData) o;
        return Objects.equals(this.id, that.id)
                && Objects.equals(this.tenantId, that.tenantId)
                && Objects.equals(this.templateRef, that.templateRef)
                && Objects.equals(this.displayName, that.displayName)
                && Objects.equals(this.fromAddress, that.fromAddress)
                && this.isActive == that.isActive
                && Objects.equals(this.content, that.content)
                && Objects.equals(this.senderReadiness, that.senderReadiness);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.tenantId, this.templateRef, this.displayName, this.fromAddress, this.isActive, this.content, this.senderReadiness);
    }

    @Override
    public String toString() {
        return "AutoReplyTemplateData[" + "id=" + this.id + ", " + "tenantId=" + this.tenantId + ", " + "templateRef=" + this.templateRef + ", " + "displayName=" + this.displayName + ", " + "fromAddress=" + this.fromAddress + ", " + "isActive=" + this.isActive + ", " + "content=" + this.content + ", " + "senderReadiness=" + this.senderReadiness + "]";
    }

    /**
     * Creates a new AutoReplyTemplateData.
     *
     * @param id template identifier
     * @param tenantId owning tenant identifier
     * @param templateRef immutable template reference
     * @param displayName human-readable template name
     * @param fromAddress sender address used for replies
     * @param isActive whether the template is active
     * @param content masked content summary
     * @param senderReadiness sender readiness for the template
     */
    @JsonCreator
    public AutoReplyTemplateData(String id, String tenantId, String templateRef, String displayName, String fromAddress, boolean isActive, AutoReplyTemplateContentSummary content, AutoReplySenderReadiness senderReadiness) {
        this.id = id;
        this.tenantId = tenantId;
        this.templateRef = templateRef;
        this.displayName = displayName;
        this.fromAddress = fromAddress;
        this.isActive = isActive;
        this.content = content;
        this.senderReadiness = senderReadiness;
    }
}
