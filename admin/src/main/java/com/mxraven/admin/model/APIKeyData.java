package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Wire representation of a submission-listener API key without secret material.
 * Prefer the {@link APIKey} entity.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class APIKeyData {
    private final String id;
    private final String tenantId;
    private final String listenerId;
    private final String username;

    /** API key identifier */
    public String id() {
        return id;
    }

    /** owning tenant identifier */
    public String tenantId() {
        return tenantId;
    }

    /** listener the key is scoped to */
    public String listenerId() {
        return listenerId;
    }

    /** submission username associated with the key */
    public String username() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        APIKeyData that = (APIKeyData) o;
        return Objects.equals(this.id, that.id)
                && Objects.equals(this.tenantId, that.tenantId)
                && Objects.equals(this.listenerId, that.listenerId)
                && Objects.equals(this.username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.tenantId, this.listenerId, this.username);
    }

    @Override
    public String toString() {
        return "APIKeyData[" + "id=" + this.id + ", " + "tenantId=" + this.tenantId + ", " + "listenerId=" + this.listenerId + ", " + "username=" + this.username + "]";
    }

    /**
     * Creates a new APIKeyData.
     *
     * @param id API key identifier
     * @param tenantId owning tenant identifier
     * @param listenerId listener the key is scoped to
     * @param username submission username associated with the key
     */
    @JsonCreator
    public APIKeyData(String id, String tenantId, String listenerId, String username) {
        this.id = id;
        this.tenantId = tenantId;
        this.listenerId = listenerId;
        this.username = username;
    }
}
