package com.mxraven.admin.model;

import java.util.List;

/**
 * Request body for approving a connected-application installation.
 *
 * @param appId application identifier
 * @param grantedScopes approved scopes; must be a subset of both the
 *                      application's requested scopes and the approver's own scopes
 */
public record CreateConnectedAppInstallationRequest(
        String appId,
        List<String> grantedScopes) {

    /**
     * Creates a request, copying the scope list.
     */
    public CreateConnectedAppInstallationRequest {
        grantedScopes = grantedScopes == null ? List.of() : List.copyOf(grantedScopes);
    }

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
