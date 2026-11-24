package com.mxraven.admin.model;

import java.util.List;

/**
 * One application installation in a workspace.
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
 * @param lastUsedAt last authenticated call, when the application has called
 */
public record ConnectedAppInstallation(
        String id,
        String tenantId,
        String appId,
        String clientId,
        String displayName,
        String homepageUrl,
        List<String> grantedScopes,
        ConnectedAppInstallationStatus status,
        ConnectedAppGrantor grantedBy,
        String grantedAt,
        String lastUsedAt) {

    /**
     * Creates an installation, copying the granted-scope list.
     */
    public ConnectedAppInstallation {
        grantedScopes = grantedScopes == null ? List.of() : List.copyOf(grantedScopes);
    }
}
