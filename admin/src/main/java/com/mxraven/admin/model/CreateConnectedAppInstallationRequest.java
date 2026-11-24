package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;
import java.util.List;

/**
 * Request body for approving a connected-application installation.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CreateConnectedAppInstallationRequest {
    private final String appId;
    private final List<String> grantedScopes;

    /** application identifier */
    public String appId() {
        return appId;
    }

    public List<String> grantedScopes() {
        return grantedScopes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateConnectedAppInstallationRequest that = (CreateConnectedAppInstallationRequest) o;
        return Objects.equals(this.appId, that.appId) && Objects.equals(this.grantedScopes, that.grantedScopes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.appId, this.grantedScopes);
    }

    @Override
    public String toString() {
        return "CreateConnectedAppInstallationRequest[" + "appId=" + this.appId + ", " + "grantedScopes=" + this.grantedScopes + "]";
    }

    /**
     * Creates a new CreateConnectedAppInstallationRequest.
     *
     * @param appId application identifier
     * @param grantedScopes grantedScopes
     */
    @JsonCreator
    public CreateConnectedAppInstallationRequest(String appId, List<String> grantedScopes) {
        this.appId = appId;
        this.grantedScopes = grantedScopes == null ? Java8.list() : Java8.copyList(grantedScopes);
    }

    /**
         * Creates a request, copying the scope list.
         */


        /**
         * Creates an installation request.
         *
         * @param appId application identifier
         * @param grantedScopes approved scopes
         * @return the request
         */
        public static CreateConnectedAppInstallationRequest of(String appId, List<String> grantedScopes) {
            return new CreateConnectedAppInstallationRequest(appId, grantedScopes);
        }
}
