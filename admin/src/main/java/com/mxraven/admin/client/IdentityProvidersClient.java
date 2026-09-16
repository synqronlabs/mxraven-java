package com.mxraven.admin.client;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.CreateTenantAppleIdentityProviderRequest;
import com.mxraven.admin.model.CreateTenantAzureAdIdentityProviderRequest;
import com.mxraven.admin.model.CreateTenantGitHubEnterpriseServerIdentityProviderRequest;
import com.mxraven.admin.model.CreateTenantGitHubIdentityProviderRequest;
import com.mxraven.admin.model.CreateTenantGitLabIdentityProviderRequest;
import com.mxraven.admin.model.CreateTenantGitLabSelfHostedIdentityProviderRequest;
import com.mxraven.admin.model.CreateTenantGoogleIdentityProviderRequest;
import com.mxraven.admin.model.CreateTenantIdentityProviderRequest;
import com.mxraven.admin.model.CreateTenantJwtIdentityProviderRequest;
import com.mxraven.admin.model.CreateTenantLdapIdentityProviderRequest;
import com.mxraven.admin.model.CreateTenantOAuthIdentityProviderRequest;
import com.mxraven.admin.model.CreateTenantSamlIdentityProviderRequest;
import com.mxraven.admin.model.IdentityAccessClaim;
import com.mxraven.admin.model.IdentityProvisioning;
import com.mxraven.admin.model.TenantIdentityProvider;
import com.mxraven.admin.model.TenantIdentityProviderData;
import com.mxraven.admin.model.UpdateIdentityProviderAutoGrantRequest;
import com.mxraven.admin.model.UpdateIdentityProvisioningRequest;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;

/**
 * Tenant identity-provider collection. Reads and creation return hydrated
 * {@link TenantIdentityProvider} entities. Creation of non-OIDC providers uses
 * the type-specific {@code /identity-providers/{type}} endpoints and is accepted
 * for durable reconciliation ({@code 202}).
 */
public final class IdentityProvidersClient {
    private final AdminClient client;
    private final String tenantSlug;

    /**
     * Create a client for the identity-provider collection of a tenant.
     *
     * @param client underlying admin client
     * @param tenantSlug tenant slug whose identity providers are accessed
     */
    public IdentityProvidersClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    /**
     * List the identity providers (first page fetched eagerly, remaining pages
     * lazy).
     *
     * @return a lazily paginating collection of identity providers
     * @throws IOException if the first page request fails or is interrupted
     */
    public Paged<TenantIdentityProvider> list() throws IOException {
        return list(null);
    }

    Paged<TenantIdentityProvider> list(QueryParams params) throws IOException {
        return client.paged(base(), params == null ? null : params.toMap(), TenantIdentityProviderData.class)
                .map(data -> new TenantIdentityProvider(client, one(data.id()), data));
    }

    /**
     * Get an identity provider by its identifier.
     *
     * @param idpId identity-provider identifier
     * @return the hydrated identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider get(String idpId) throws IOException {
        String resourcePath = one(idpId);
        return new TenantIdentityProvider(client, resourcePath,
                client.get(resourcePath).as(TenantIdentityProviderData.class));
    }

    // --- create (OIDC) -------------------------------------------------------

    /**
     * Create an OIDC identity provider from a configured builder.
     *
     * @param configure consumer that configures the request builder
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider create(Consumer<CreateTenantIdentityProviderRequest.Builder> configure)
            throws IOException {
        CreateTenantIdentityProviderRequest.Builder builder = CreateTenantIdentityProviderRequest.builder();
        configure.accept(builder);
        return create(builder.build());
    }

    /**
     * Create an OIDC identity provider.
     *
     * @param request creation request
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider create(CreateTenantIdentityProviderRequest request) throws IOException {
        TenantIdentityProviderData data = client.post(base(), request).as(TenantIdentityProviderData.class);
        return new TenantIdentityProvider(client, one(data.id()), data);
    }

    // --- create (type-specific) ---------------------------------------------

    /**
     * Create an OAuth identity provider.
     *
     * @param request creation request
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider createOAuth(CreateTenantOAuthIdentityProviderRequest request) throws IOException {
        return createProvider("oauth", request);
    }

    /**
     * Create an OAuth identity provider from a configured builder.
     *
     * @param configure consumer that configures the request builder
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider createOAuth(Consumer<CreateTenantOAuthIdentityProviderRequest.Builder> configure)
            throws IOException {
        CreateTenantOAuthIdentityProviderRequest.Builder builder = CreateTenantOAuthIdentityProviderRequest.builder();
        configure.accept(builder);
        return createOAuth(builder.build());
    }

    /**
     * Create a JWT identity provider.
     *
     * @param request creation request
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider createJwt(CreateTenantJwtIdentityProviderRequest request) throws IOException {
        return createProvider("jwt", request);
    }

    /**
     * Create a JWT identity provider from a configured builder.
     *
     * @param configure consumer that configures the request builder
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider createJwt(Consumer<CreateTenantJwtIdentityProviderRequest.Builder> configure)
            throws IOException {
        CreateTenantJwtIdentityProviderRequest.Builder builder = CreateTenantJwtIdentityProviderRequest.builder();
        configure.accept(builder);
        return createJwt(builder.build());
    }

    /**
     * Create a SAML identity provider.
     *
     * @param request creation request
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider createSaml(CreateTenantSamlIdentityProviderRequest request) throws IOException {
        return createProvider("saml", request);
    }

    /**
     * Create a SAML identity provider from a configured builder.
     *
     * @param configure consumer that configures the request builder
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider createSaml(Consumer<CreateTenantSamlIdentityProviderRequest.Builder> configure)
            throws IOException {
        CreateTenantSamlIdentityProviderRequest.Builder builder = CreateTenantSamlIdentityProviderRequest.builder();
        configure.accept(builder);
        return createSaml(builder.build());
    }

    /**
     * Create an LDAP identity provider.
     *
     * @param request creation request
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider createLdap(CreateTenantLdapIdentityProviderRequest request) throws IOException {
        return createProvider("ldap", request);
    }

    /**
     * Create an LDAP identity provider from a configured builder.
     *
     * @param configure consumer that configures the request builder
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider createLdap(Consumer<CreateTenantLdapIdentityProviderRequest.Builder> configure)
            throws IOException {
        CreateTenantLdapIdentityProviderRequest.Builder builder = CreateTenantLdapIdentityProviderRequest.builder();
        configure.accept(builder);
        return createLdap(builder.build());
    }

    /**
     * Create a Google identity provider.
     *
     * @param request creation request
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider createGoogle(CreateTenantGoogleIdentityProviderRequest request) throws IOException {
        return createProvider("google", request);
    }

    /**
     * Create a Google identity provider from a configured builder.
     *
     * @param configure consumer that configures the request builder
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider createGoogle(Consumer<CreateTenantGoogleIdentityProviderRequest.Builder> configure)
            throws IOException {
        CreateTenantGoogleIdentityProviderRequest.Builder builder = CreateTenantGoogleIdentityProviderRequest.builder();
        configure.accept(builder);
        return createGoogle(builder.build());
    }

    /**
     * Create an Azure AD identity provider.
     *
     * @param request creation request
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider createAzureAd(CreateTenantAzureAdIdentityProviderRequest request)
            throws IOException {
        return createProvider("azure-ad", request);
    }

    /**
     * Create an Azure AD identity provider from a configured builder.
     *
     * @param configure consumer that configures the request builder
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider createAzureAd(Consumer<CreateTenantAzureAdIdentityProviderRequest.Builder> configure)
            throws IOException {
        CreateTenantAzureAdIdentityProviderRequest.Builder builder = CreateTenantAzureAdIdentityProviderRequest.builder();
        configure.accept(builder);
        return createAzureAd(builder.build());
    }

    /**
     * Create a GitHub identity provider.
     *
     * @param request creation request
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider createGitHub(CreateTenantGitHubIdentityProviderRequest request)
            throws IOException {
        return createProvider("github", request);
    }

    /**
     * Create a GitHub identity provider from a configured builder.
     *
     * @param configure consumer that configures the request builder
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider createGitHub(Consumer<CreateTenantGitHubIdentityProviderRequest.Builder> configure)
            throws IOException {
        CreateTenantGitHubIdentityProviderRequest.Builder builder = CreateTenantGitHubIdentityProviderRequest.builder();
        configure.accept(builder);
        return createGitHub(builder.build());
    }

    /**
     * Create a GitHub Enterprise Server identity provider.
     *
     * @param request creation request
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider createGitHubEnterpriseServer(
            CreateTenantGitHubEnterpriseServerIdentityProviderRequest request) throws IOException {
        return createProvider("github-enterprise-server", request);
    }

    /**
     * Create a GitHub Enterprise Server identity provider from a configured
     * builder.
     *
     * @param configure consumer that configures the request builder
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider createGitHubEnterpriseServer(
            Consumer<CreateTenantGitHubEnterpriseServerIdentityProviderRequest.Builder> configure) throws IOException {
        CreateTenantGitHubEnterpriseServerIdentityProviderRequest.Builder builder =
                CreateTenantGitHubEnterpriseServerIdentityProviderRequest.builder();
        configure.accept(builder);
        return createGitHubEnterpriseServer(builder.build());
    }

    /**
     * Create a GitLab identity provider.
     *
     * @param request creation request
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider createGitLab(CreateTenantGitLabIdentityProviderRequest request)
            throws IOException {
        return createProvider("gitlab", request);
    }

    /**
     * Create a GitLab identity provider from a configured builder.
     *
     * @param configure consumer that configures the request builder
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider createGitLab(Consumer<CreateTenantGitLabIdentityProviderRequest.Builder> configure)
            throws IOException {
        CreateTenantGitLabIdentityProviderRequest.Builder builder = CreateTenantGitLabIdentityProviderRequest.builder();
        configure.accept(builder);
        return createGitLab(builder.build());
    }

    /**
     * Create a GitLab Self-Hosted identity provider.
     *
     * @param request creation request
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider createGitLabSelfHosted(
            CreateTenantGitLabSelfHostedIdentityProviderRequest request) throws IOException {
        return createProvider("gitlab-self-hosted", request);
    }

    /**
     * Create a GitLab Self-Hosted identity provider from a configured builder.
     *
     * @param configure consumer that configures the request builder
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider createGitLabSelfHosted(
            Consumer<CreateTenantGitLabSelfHostedIdentityProviderRequest.Builder> configure) throws IOException {
        CreateTenantGitLabSelfHostedIdentityProviderRequest.Builder builder =
                CreateTenantGitLabSelfHostedIdentityProviderRequest.builder();
        configure.accept(builder);
        return createGitLabSelfHosted(builder.build());
    }

    /**
     * Create an Apple identity provider.
     *
     * @param request creation request
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider createApple(CreateTenantAppleIdentityProviderRequest request) throws IOException {
        return createProvider("apple", request);
    }

    /**
     * Create an Apple identity provider from a configured builder.
     *
     * @param configure consumer that configures the request builder
     * @return the created identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider createApple(Consumer<CreateTenantAppleIdentityProviderRequest.Builder> configure)
            throws IOException {
        CreateTenantAppleIdentityProviderRequest.Builder builder = CreateTenantAppleIdentityProviderRequest.builder();
        configure.accept(builder);
        return createApple(builder.build());
    }

    private TenantIdentityProvider createProvider(String segment, Object request) throws IOException {
        TenantIdentityProviderData data = client.post(base() + "/" + segment, request)
                .as(TenantIdentityProviderData.class);
        return new TenantIdentityProvider(client, one(data.id()), data);
    }

    // --- provisioning, auto-grant, claim ------------------------------------

    /**
     * Get the tenant's identity-provisioning configuration.
     *
     * @return the identity-provisioning state
     * @throws IOException if the request fails or is interrupted
     */
    public IdentityProvisioning getProvisioning() throws IOException {
        return client.get(provisioningPath()).as(IdentityProvisioning.class);
    }

    /**
     * Replace the tenant's identity-provisioning roles.
     *
     * @param roles provisioning roles to set
     * @return the updated identity-provisioning state
     * @throws IOException if the request fails or is interrupted
     */
    public IdentityProvisioning updateProvisioning(List<String> roles) throws IOException {
        return updateProvisioning(new UpdateIdentityProvisioningRequest(roles));
    }

    /**
     * Replace the tenant's identity-provisioning configuration.
     *
     * @param request update request
     * @return the updated identity-provisioning state
     * @throws IOException if the request fails or is interrupted
     */
    public IdentityProvisioning updateProvisioning(UpdateIdentityProvisioningRequest request) throws IOException {
        return client.put(provisioningPath(), request).as(IdentityProvisioning.class);
    }

    /**
     * Replace the tenant's identity-provisioning configuration from a configured
     * builder.
     *
     * @param configure consumer that configures the request builder
     * @return the updated identity-provisioning state
     * @throws IOException if the request fails or is interrupted
     */
    public IdentityProvisioning updateProvisioning(Consumer<UpdateIdentityProvisioningRequest.Builder> configure)
            throws IOException {
        UpdateIdentityProvisioningRequest.Builder builder = UpdateIdentityProvisioningRequest.builder();
        configure.accept(builder);
        return updateProvisioning(builder.build());
    }

    /**
     * Claim workspace access for an identity-provider-provisioned user.
     *
     * @return the claimed access state
     * @throws IOException if the request fails or is interrupted
     */
    public IdentityAccessClaim claimAccess() throws IOException {
        return client.post("/tenants/" + seg(tenantSlug) + "/identity/claim-access", null)
                .as(IdentityAccessClaim.class);
    }

    /**
     * Enable or disable automatic granting for an identity provider.
     *
     * @param idpId identity-provider identifier
     * @param enabled desired auto-grant state
     * @return the updated identity provider
     * @throws IOException if the request fails or is interrupted
     */
    public TenantIdentityProvider setAutoGrant(String idpId, boolean enabled) throws IOException {
        TenantIdentityProviderData data = client.put(one(idpId) + "/auto-grant",
                new UpdateIdentityProviderAutoGrantRequest(enabled)).as(TenantIdentityProviderData.class);
        return new TenantIdentityProvider(client, one(idpId), data);
    }

    private String base() {
        return "/tenants/" + seg(tenantSlug) + "/identity-providers";
    }

    private String provisioningPath() {
        return "/tenants/" + seg(tenantSlug) + "/identity-provisioning";
    }

    private String one(String id) {
        return base() + "/" + seg(id);
    }

    private static String seg(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
