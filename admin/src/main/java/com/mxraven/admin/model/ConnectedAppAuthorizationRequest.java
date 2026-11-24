package com.mxraven.admin.model;

import java.util.List;

/**
 * Preview of a browser (PKCE) authorization request. Nothing is approved by
 * fetching it; approval is {@link CreateConnectedAppInstallationRequest}.
 *
 * @param appId application identifier
 * @param clientId identity-provider client identifier
 * @param redirectUri exact registered callback the approval hands off to
 * @param displayName application display name
 * @param description application description
 * @param homepageUrl application homepage
 * @param requestedScopes the registration's scope ceiling, exactly what approval grants
 */
public record ConnectedAppAuthorizationRequest(
        String appId,
        String clientId,
        String redirectUri,
        String displayName,
        String description,
        String homepageUrl,
        List<ConnectedAppScope> requestedScopes) {

    /**
     * Creates a preview, copying the scope list.
     */
    public ConnectedAppAuthorizationRequest {
        requestedScopes = requestedScopes == null ? List.of() : List.copyOf(requestedScopes);
    }
}
