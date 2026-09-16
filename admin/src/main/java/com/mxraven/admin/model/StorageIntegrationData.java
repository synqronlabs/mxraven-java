package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Wire representation backing the {@link StorageIntegration} entity.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class StorageIntegrationData {
    private final String id;
    private final String tenantId;
    private final String storageRef;
    private final String displayName;
    private final boolean isActive;
    private final boolean credentialsPresent;

    /** unique integration identifier */
    public String id() {
        return id;
    }

    /** identifier of the owning tenant */
    public String tenantId() {
        return tenantId;
    }

    /** stable reference for the integration */
    public String storageRef() {
        return storageRef;
    }

    /** human-readable integration name */
    public String displayName() {
        return displayName;
    }

    /** whether the integration is active */
    public boolean isActive() {
        return isActive;
    }

    /** whether storage credentials are stored */
    public boolean credentialsPresent() {
        return credentialsPresent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StorageIntegrationData that = (StorageIntegrationData) o;
        return Objects.equals(this.id, that.id)
                && Objects.equals(this.tenantId, that.tenantId)
                && Objects.equals(this.storageRef, that.storageRef)
                && Objects.equals(this.displayName, that.displayName)
                && this.isActive == that.isActive
                && this.credentialsPresent == that.credentialsPresent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.tenantId, this.storageRef, this.displayName, this.isActive, this.credentialsPresent);
    }

    @Override
    public String toString() {
        return "StorageIntegrationData[" + "id=" + this.id + ", " + "tenantId=" + this.tenantId + ", " + "storageRef=" + this.storageRef + ", " + "displayName=" + this.displayName + ", " + "isActive=" + this.isActive + ", " + "credentialsPresent=" + this.credentialsPresent + "]";
    }

    /**
     * Creates a new StorageIntegrationData.
     *
     * @param id unique integration identifier
     * @param tenantId identifier of the owning tenant
     * @param storageRef stable reference for the integration
     * @param displayName human-readable integration name
     * @param isActive whether the integration is active
     * @param credentialsPresent whether storage credentials are stored
     */
    @JsonCreator
    public StorageIntegrationData(String id, String tenantId, String storageRef, String displayName, boolean isActive, boolean credentialsPresent) {
        this.id = id;
        this.tenantId = tenantId;
        this.storageRef = storageRef;
        this.displayName = displayName;
        this.isActive = isActive;
        this.credentialsPresent = credentialsPresent;
    }
}
