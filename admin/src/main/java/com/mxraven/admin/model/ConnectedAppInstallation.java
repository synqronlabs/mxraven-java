package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;
import java.util.List;

/**
 * One application installation in a workspace.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ConnectedAppInstallation {
    private final String id;
    private final String tenantId;
    private final String appId;
    private final String clientId;
    private final String displayName;
    private final String homepageUrl;
    private final List<String> grantedScopes;
    private final ConnectedAppInstallationStatus status;
    private final ConnectedAppGrantor grantedBy;
    private final String grantedAt;
    private final String lastUsedAt;

    /** installation identifier */
    public String id() {
        return id;
    }

    /** owning tenant */
    public String tenantId() {
        return tenantId;
    }

    /** registered application identifier */
    public String appId() {
        return appId;
    }

    /** identity-provider client identifier */
    public String clientId() {
        return clientId;
    }

    /** application display name */
    public String displayName() {
        return displayName;
    }

    /** application homepage */
    public String homepageUrl() {
        return homepageUrl;
    }

    /** scopes approved for this installation */
    public List<String> grantedScopes() {
        return grantedScopes;
    }

    /** lifecycle state */
    public ConnectedAppInstallationStatus status() {
        return status;
    }

    /** principal that approved the installation */
    public ConnectedAppGrantor grantedBy() {
        return grantedBy;
    }

    /** approval timestamp */
    public String grantedAt() {
        return grantedAt;
    }

    public String lastUsedAt() {
        return lastUsedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConnectedAppInstallation that = (ConnectedAppInstallation) o;
        return Objects.equals(this.id, that.id) && Objects.equals(this.tenantId, that.tenantId) && Objects.equals(this.appId, that.appId) && Objects.equals(this.clientId, that.clientId) && Objects.equals(this.displayName, that.displayName) && Objects.equals(this.homepageUrl, that.homepageUrl) && Objects.equals(this.grantedScopes, that.grantedScopes) && Objects.equals(this.status, that.status) && Objects.equals(this.grantedBy, that.grantedBy) && Objects.equals(this.grantedAt, that.grantedAt) && Objects.equals(this.lastUsedAt, that.lastUsedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.tenantId, this.appId, this.clientId, this.displayName, this.homepageUrl, this.grantedScopes, this.status, this.grantedBy, this.grantedAt, this.lastUsedAt);
    }

    @Override
    public String toString() {
        return "ConnectedAppInstallation[" + "id=" + this.id + ", " + "tenantId=" + this.tenantId + ", " + "appId=" + this.appId + ", " + "clientId=" + this.clientId + ", " + "displayName=" + this.displayName + ", " + "homepageUrl=" + this.homepageUrl + ", " + "grantedScopes=" + this.grantedScopes + ", " + "status=" + this.status + ", " + "grantedBy=" + this.grantedBy + ", " + "grantedAt=" + this.grantedAt + ", " + "lastUsedAt=" + this.lastUsedAt + "]";
    }

    /**
     * Creates a new ConnectedAppInstallation.
     *
     * @param id installation identifier
     * @param tenantId owning tenant
     * @param appId registered application identifier
     * @param clientId identity-provider client identifier
     * @param displayName application display name
     * @param homepageUrl application homepage
     * @param grantedScopes scopes approved for this installation
     * @param status lifecycle state
     * @param grantedBy principal that approved the installation
     * @param grantedAt approval timestamp
     * @param lastUsedAt lastUsedAt
     */
    @JsonCreator
    public ConnectedAppInstallation(String id, String tenantId, String appId, String clientId, String displayName, String homepageUrl, List<String> grantedScopes, ConnectedAppInstallationStatus status, ConnectedAppGrantor grantedBy, String grantedAt, String lastUsedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.appId = appId;
        this.clientId = clientId;
        this.displayName = displayName;
        this.homepageUrl = homepageUrl;
        this.status = status;
        this.grantedBy = grantedBy;
        this.grantedAt = grantedAt;
        this.lastUsedAt = lastUsedAt;
        this.grantedScopes = grantedScopes == null ? Java8.list() : Java8.copyList(grantedScopes);
    }

    /**
         * Creates an installation, copying the granted-scope list.
         */
}
