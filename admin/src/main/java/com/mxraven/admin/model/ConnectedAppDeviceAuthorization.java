package com.mxraven.admin.model;

import java.util.List;

/**
 * Preview of a pending device authorization. Nothing is approved by fetching
 * it; approval is {@link CreateConnectedAppInstallationRequest}.
 *
 * @param userCode code the user confirms at the identity provider after approval
 * @param appId application identifier
 * @param clientId identity-provider client identifier
 * @param displayName application display name
 * @param description application description
 * @param homepageUrl application homepage
 * @param requestedScopes the registration's scope ceiling, exactly what approval grants
 */
public record ConnectedAppDeviceAuthorization(
        String userCode,
        String appId,
        String clientId,
        String displayName,
        String description,
        String homepageUrl,
        List<ConnectedAppScope> requestedScopes) {

    /**
     * Creates a preview, copying the scope list.
     */
    public ConnectedAppDeviceAuthorization {
        requestedScopes = requestedScopes == null ? List.of() : List.copyOf(requestedScopes);
    }
}
