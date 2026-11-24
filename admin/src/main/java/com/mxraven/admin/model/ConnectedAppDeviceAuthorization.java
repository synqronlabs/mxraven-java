package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;
import java.util.List;

/**
 * Preview of a pending device authorization. Nothing is approved by fetching
 * it; approval is {@link CreateConnectedAppInstallationRequest}.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ConnectedAppDeviceAuthorization {
    private final String userCode;
    private final String appId;
    private final String clientId;
    private final String displayName;
    private final String description;
    private final String homepageUrl;
    private final List<ConnectedAppScope> requestedScopes;

    /** code the user confirms at the identity provider after approval */
    public String userCode() {
        return userCode;
    }

    /** application identifier */
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

    /** application description */
    public String description() {
        return description;
    }

    /** application homepage */
    public String homepageUrl() {
        return homepageUrl;
    }

    public List<ConnectedAppScope> requestedScopes() {
        return requestedScopes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConnectedAppDeviceAuthorization that = (ConnectedAppDeviceAuthorization) o;
        return Objects.equals(this.userCode, that.userCode) && Objects.equals(this.appId, that.appId) && Objects.equals(this.clientId, that.clientId) && Objects.equals(this.displayName, that.displayName) && Objects.equals(this.description, that.description) && Objects.equals(this.homepageUrl, that.homepageUrl) && Objects.equals(this.requestedScopes, that.requestedScopes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.userCode, this.appId, this.clientId, this.displayName, this.description, this.homepageUrl, this.requestedScopes);
    }

    @Override
    public String toString() {
        return "ConnectedAppDeviceAuthorization[" + "userCode=" + this.userCode + ", " + "appId=" + this.appId + ", " + "clientId=" + this.clientId + ", " + "displayName=" + this.displayName + ", " + "description=" + this.description + ", " + "homepageUrl=" + this.homepageUrl + ", " + "requestedScopes=" + this.requestedScopes + "]";
    }

    /**
     * Creates a new ConnectedAppDeviceAuthorization.
     *
     * @param userCode code the user confirms at the identity provider after approval
     * @param appId application identifier
     * @param clientId identity-provider client identifier
     * @param displayName application display name
     * @param description application description
     * @param homepageUrl application homepage
     * @param requestedScopes requestedScopes
     */
    @JsonCreator
    public ConnectedAppDeviceAuthorization(String userCode, String appId, String clientId, String displayName, String description, String homepageUrl, List<ConnectedAppScope> requestedScopes) {
        this.userCode = userCode;
        this.appId = appId;
        this.clientId = clientId;
        this.displayName = displayName;
        this.description = description;
        this.homepageUrl = homepageUrl;
        this.requestedScopes = requestedScopes == null ? Java8.list() : Java8.copyList(requestedScopes);
    }

    /**
         * Creates a preview, copying the scope list.
         */
}
