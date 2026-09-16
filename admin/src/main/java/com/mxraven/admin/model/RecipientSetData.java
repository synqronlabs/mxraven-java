package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Wire representation backing the {@link RecipientSet} entity.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class RecipientSetData {
    private final String id;
    private final String tenantId;
    private final String setRef;
    private final String displayName;
    private final String description;
    private final String createdAt;
    private final String updatedAt;

    /** identifier of the recipient set */
    public String id() {
        return id;
    }

    /** identifier of the owning tenant */
    public String tenantId() {
        return tenantId;
    }

    /** stable reference used to address the set */
    public String setRef() {
        return setRef;
    }

    /** human-readable name */
    public String displayName() {
        return displayName;
    }

    /** optional description */
    public String description() {
        return description;
    }

    /** creation timestamp */
    public String createdAt() {
        return createdAt;
    }

    /** last update timestamp */
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
        RecipientSetData that = (RecipientSetData) o;
        return Objects.equals(this.id, that.id)
                && Objects.equals(this.tenantId, that.tenantId)
                && Objects.equals(this.setRef, that.setRef)
                && Objects.equals(this.displayName, that.displayName)
                && Objects.equals(this.description, that.description)
                && Objects.equals(this.createdAt, that.createdAt)
                && Objects.equals(this.updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.tenantId, this.setRef, this.displayName, this.description, this.createdAt, this.updatedAt);
    }

    @Override
    public String toString() {
        return "RecipientSetData[" + "id=" + this.id + ", " + "tenantId=" + this.tenantId + ", " + "setRef=" + this.setRef + ", " + "displayName=" + this.displayName + ", " + "description=" + this.description + ", " + "createdAt=" + this.createdAt + ", " + "updatedAt=" + this.updatedAt + "]";
    }

    /**
     * Creates a new RecipientSetData.
     *
     * @param id identifier of the recipient set
     * @param tenantId identifier of the owning tenant
     * @param setRef stable reference used to address the set
     * @param displayName human-readable name
     * @param description optional description
     * @param createdAt creation timestamp
     * @param updatedAt last update timestamp
     */
    @JsonCreator
    public RecipientSetData(String id, String tenantId, String setRef, String displayName, String description, String createdAt, String updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.setRef = setRef;
        this.displayName = displayName;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
