package com.mxraven.admin.client;

import com.mxraven.admin.internal.Java8;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.ConnectedAppAuthorizationRequest;
import com.mxraven.admin.model.ConnectedAppDeviceAuthorization;
import com.mxraven.admin.model.ConnectedAppInstallation;
import com.mxraven.admin.model.CreateConnectedAppInstallationRequest;

import java.io.IOException;

/**
 * Applications connected to a workspace.
 *
 * <p>An application asks for access through a device authorization or a
 * browser authorization request. Both are previews: an approval is
 * {@link #create(CreateConnectedAppInstallationRequest)}, after which the
 * application can call the control plane with the approved scopes.
 */
public final class ConnectedAppsClient {
    private final AdminClient client;
    private final String tenantSlug;

    /**
     * Creates a client bound to the given tenant.
     *
     * @param client underlying admin client
     * @param tenantSlug tenant slug
     */
    public ConnectedAppsClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    /**
     * Lists live installations, newest first.
     *
     * @return a lazily paginated collection of installations
     * @throws IOException if the first page request fails or is interrupted
     */
    public Paged<ConnectedAppInstallation> list() throws IOException {
        return list(null);
    }

    Paged<ConnectedAppInstallation> list(QueryParams params) throws IOException {
        return client.paged(base(), params == null ? null : params.toMap(), ConnectedAppInstallation.class);
    }

    /**
     * Approves an application installation.
     *
     * <p>Approval is idempotent per application and workspace: approving again
     * replaces the live installation in place, keeping its identifier and
     * re-recording the granted scopes. A revoked installation does not conflict
     * either; approving after revocation records a new consent.
     *
     * @param request installation approval request
     * @return the approved installation
     * @throws IOException if the request fails or is interrupted
     */
    public ConnectedAppInstallation create(CreateConnectedAppInstallationRequest request) throws IOException {
        return client.post(base(), request).as(ConnectedAppInstallation.class);
    }

    /**
     * Gets an installation by its identifier.
     *
     * @param installationId installation identifier
     * @return the installation
     * @throws IOException if the request fails or is interrupted
     */
    public ConnectedAppInstallation get(String installationId) throws IOException {
        return client.get(one(installationId)).as(ConnectedAppInstallation.class);
    }

    /**
     * Revokes an installation. Access ends immediately; the revoked grant is
     * retained as history.
     *
     * @param installationId installation identifier
     * @throws IOException if the request fails or is interrupted
     */
    public void revoke(String installationId) throws IOException {
        client.delete(one(installationId));
    }

    /**
     * Previews a pending device authorization for the consent screen.
     *
     * <p>Nothing is approved here; approve with
     * {@link #create(CreateConnectedAppInstallationRequest)}.
     *
     * @param userCode user code the application displayed
     * @return the pending authorization request
     * @throws IOException if the request fails or is interrupted
     */
    public ConnectedAppDeviceAuthorization getDeviceAuthorization(String userCode) throws IOException {
        return client.get(base() + "/device-authorization/" + encode(userCode))
                .as(ConnectedAppDeviceAuthorization.class);
    }

    /**
     * Validates a browser authorization request for the consent screen.
     *
     * <p>Nothing is approved here and no authorization code is issued; approve
     * with {@link #create(CreateConnectedAppInstallationRequest)}.
     *
     * @param clientId application client identifier
     * @param redirectUri exact registered callback URI
     * @return the validated authorization request
     * @throws IOException if the request fails or is interrupted
     */
    public ConnectedAppAuthorizationRequest getAuthorizationRequest(String clientId, String redirectUri)
            throws IOException {
        java.util.Map<String, String> query = new java.util.LinkedHashMap<>();
        query.put("client_id", clientId);
        query.put("redirect_uri", redirectUri);
        return client.get(base() + "/authorization-request", query)
                .as(ConnectedAppAuthorizationRequest.class);
    }

    private String base() {
        return "/tenants/" + encode(tenantSlug) + "/connected-apps";
    }

    private String one(String installationId) {
        return base() + "/" + encode(installationId);
    }

    private static String encode(String segment) {
        return Java8.urlEncode(segment);
    }
}
