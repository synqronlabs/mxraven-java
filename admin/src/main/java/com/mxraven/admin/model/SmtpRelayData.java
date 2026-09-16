package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Wire representation backing the {@link SmtpRelay} entity.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class SmtpRelayData {
    private final String id;
    private final String tenantId;
    private final String relayRef;
    private final String displayName;
    private final boolean isActive;
    private final boolean credentialsPresent;

    /** unique relay identifier */
    public String id() {
        return id;
    }

    /** identifier of the owning tenant */
    public String tenantId() {
        return tenantId;
    }

    /** stable reference for the relay */
    public String relayRef() {
        return relayRef;
    }

    /** human-readable relay name */
    public String displayName() {
        return displayName;
    }

    /** whether the relay is active */
    public boolean isActive() {
        return isActive;
    }

    /** whether relay credentials are stored */
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
        SmtpRelayData that = (SmtpRelayData) o;
        return Objects.equals(this.id, that.id)
                && Objects.equals(this.tenantId, that.tenantId)
                && Objects.equals(this.relayRef, that.relayRef)
                && Objects.equals(this.displayName, that.displayName)
                && this.isActive == that.isActive
                && this.credentialsPresent == that.credentialsPresent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.tenantId, this.relayRef, this.displayName, this.isActive, this.credentialsPresent);
    }

    @Override
    public String toString() {
        return "SmtpRelayData[" + "id=" + this.id + ", " + "tenantId=" + this.tenantId + ", " + "relayRef=" + this.relayRef + ", " + "displayName=" + this.displayName + ", " + "isActive=" + this.isActive + ", " + "credentialsPresent=" + this.credentialsPresent + "]";
    }

    /**
     * Creates a new SmtpRelayData.
     *
     * @param id unique relay identifier
     * @param tenantId identifier of the owning tenant
     * @param relayRef stable reference for the relay
     * @param displayName human-readable relay name
     * @param isActive whether the relay is active
     * @param credentialsPresent whether relay credentials are stored
     */
    @JsonCreator
    public SmtpRelayData(String id, String tenantId, String relayRef, String displayName, boolean isActive, boolean credentialsPresent) {
        this.id = id;
        this.tenantId = tenantId;
        this.relayRef = relayRef;
        this.displayName = displayName;
        this.isActive = isActive;
        this.credentialsPresent = credentialsPresent;
    }
}
