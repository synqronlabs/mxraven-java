package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.Map;

/**
 * One tenant audit log entry.
 *
 * <p>{@code actorKind} is one of {@code human}, {@code machine}, or
 * {@code system}; {@code status} is {@code success} or {@code failure}.
 * {@code details} is action-specific free-form JSON.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class AuditLog {
    private final String id;
    private final String tenantId;
    private final String actorId;
    private final AuditActorKind actorKind;
    private final String action;
    private final String resourceType;
    private final String resourceId;
    private final AuditStatus status;
    private final Map<String, Object> details;
    private final String createdAt;

    /** unique audit-log identifier */
    public String id() {
        return id;
    }

    /** owning tenant identifier */
    public String tenantId() {
        return tenantId;
    }

    /** identifier of the acting user, machine, or system */
    public String actorId() {
        return actorId;
    }

    /** kind of actor that produced the entry */
    public AuditActorKind actorKind() {
        return actorKind;
    }

    /** action that was performed */
    public String action() {
        return action;
    }

    /** type of the affected resource */
    public String resourceType() {
        return resourceType;
    }

    /** identifier of the affected resource */
    public String resourceId() {
        return resourceId;
    }

    /** outcome of the action */
    public AuditStatus status() {
        return status;
    }

    /** action-specific free-form JSON */
    public Map<String, Object> details() {
        return details;
    }

    /** creation timestamp */
    public String createdAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuditLog that = (AuditLog) o;
        return Objects.equals(this.id, that.id)
                && Objects.equals(this.tenantId, that.tenantId)
                && Objects.equals(this.actorId, that.actorId)
                && Objects.equals(this.actorKind, that.actorKind)
                && Objects.equals(this.action, that.action)
                && Objects.equals(this.resourceType, that.resourceType)
                && Objects.equals(this.resourceId, that.resourceId)
                && Objects.equals(this.status, that.status)
                && Objects.equals(this.details, that.details)
                && Objects.equals(this.createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.tenantId, this.actorId, this.actorKind, this.action, this.resourceType, this.resourceId, this.status, this.details, this.createdAt);
    }

    @Override
    public String toString() {
        return "AuditLog[" + "id=" + this.id + ", " + "tenantId=" + this.tenantId + ", " + "actorId=" + this.actorId + ", " + "actorKind=" + this.actorKind + ", " + "action=" + this.action + ", " + "resourceType=" + this.resourceType + ", " + "resourceId=" + this.resourceId + ", " + "status=" + this.status + ", " + "details=" + this.details + ", " + "createdAt=" + this.createdAt + "]";
    }

    /**
     * Creates a new AuditLog.
     *
     * @param id unique audit-log identifier
     * @param tenantId owning tenant identifier
     * @param actorId identifier of the acting user, machine, or system
     * @param actorKind kind of actor that produced the entry
     * @param action action that was performed
     * @param resourceType type of the affected resource
     * @param resourceId identifier of the affected resource
     * @param status outcome of the action
     * @param details action-specific free-form JSON
     * @param createdAt creation timestamp
     */
    @JsonCreator
    public AuditLog(String id, String tenantId, String actorId, AuditActorKind actorKind, String action, String resourceType, String resourceId, AuditStatus status, Map<String, Object> details, String createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.actorId = actorId;
        this.actorKind = actorKind;
        this.action = action;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.status = status;
        this.details = details;
        this.createdAt = createdAt;
    }
}
