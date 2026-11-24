package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;
import java.util.List;

/**
 * Preview of a browser (PKCE) authorization request. Nothing is approved by
 * fetching it; approval is {@link CreateConnectedAppInstallationRequest}.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ConnectedAppAuthorizationRequest {
    private final String appId;
    private final String clientId;
    private final String redirectUri;
    private final String displayName;
    private final String description;
    private final String homepageUrl;
    private final List<ConnectedAppScope> requestedScopes;

    /** application identifier */
    public String appId() {
        return appId;
    }

    /** identity-provider client identifier */
    public String clientId() {
        return clientId;
    }

    /** exact registered callback the approval hands off to */
    public String redirectUri() {
        return redirectUri;
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
        ConnectedAppAuthorizationRequest that = (ConnectedAppAuthorizationRequest) o;
        return Objects.equals(this.appId, that.appId) && Objects.equals(this.clientId, that.clientId) && Objects.equals(this.redirectUri, that.redirectUri) && Objects.equals(this.displayName, that.displayName) && Objects.equals(this.description, that.description) && Objects.equals(this.homepageUrl, that.homepageUrl) && Objects.equals(this.requestedScopes, that.requestedScopes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.appId, this.clientId, this.redirectUri, this.displayName, this.description, this.homepageUrl, this.requestedScopes);
    }

    @Override
    public String toString() {
        return "ConnectedAppAuthorizationRequest[" + "appId=" + this.appId + ", " + "clientId=" + this.clientId + ", " + "redirectUri=" + this.redirectUri + ", " + "displayName=" + this.displayName + ", " + "description=" + this.description + ", " + "homepageUrl=" + this.homepageUrl + ", " + "requestedScopes=" + this.requestedScopes + "]";
    }

    /**
     * Creates a new ConnectedAppAuthorizationRequest.
     *
     * @param appId application identifier
     * @param clientId identity-provider client identifier
     * @param redirectUri exact registered callback the approval hands off to
     * @param displayName application display name
     * @param description application description
     * @param homepageUrl application homepage
     * @param requestedScopes requestedScopes
     */
    @JsonCreator
    public ConnectedAppAuthorizationRequest(String appId, String clientId, String redirectUri, String displayName, String description, String homepageUrl, List<ConnectedAppScope> requestedScopes) {
        this.appId = appId;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.displayName = displayName;
        this.description = description;
        this.homepageUrl = homepageUrl;
        this.requestedScopes = requestedScopes == null ? Java8.list() : Java8.copyList(requestedScopes);
    }

    /**
         * Creates a preview, copying the scope list.
         */
}
