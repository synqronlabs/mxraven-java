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

    public IdentityProvidersClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    public Paged<TenantIdentityProvider> list() throws IOException {
        return list(null);
    }

    Paged<TenantIdentityProvider> list(QueryParams params) throws IOException {
        return client.paged(base(), params == null ? null : params.toMap(), TenantIdentityProviderData.class)
                .map(data -> new TenantIdentityProvider(client, one(data.id()), data));
    }

    public TenantIdentityProvider get(String idpId) throws IOException {
        String resourcePath = one(idpId);
        return new TenantIdentityProvider(client, resourcePath,
                client.get(resourcePath).as(TenantIdentityProviderData.class));
    }

    // --- create (OIDC) -------------------------------------------------------

    public TenantIdentityProvider create(Consumer<CreateTenantIdentityProviderRequest.Builder> configure)
            throws IOException {
        CreateTenantIdentityProviderRequest.Builder builder = CreateTenantIdentityProviderRequest.builder();
        configure.accept(builder);
        return create(builder.build());
    }

    public TenantIdentityProvider create(CreateTenantIdentityProviderRequest request) throws IOException {
        TenantIdentityProviderData data = client.post(base(), request).as(TenantIdentityProviderData.class);
        return new TenantIdentityProvider(client, one(data.id()), data);
    }

    // --- create (type-specific) ---------------------------------------------

    public TenantIdentityProvider createOAuth(CreateTenantOAuthIdentityProviderRequest request) throws IOException {
        return createProvider("oauth", request);
    }

    public TenantIdentityProvider createOAuth(Consumer<CreateTenantOAuthIdentityProviderRequest.Builder> configure)
            throws IOException {
        CreateTenantOAuthIdentityProviderRequest.Builder builder = CreateTenantOAuthIdentityProviderRequest.builder();
        configure.accept(builder);
        return createOAuth(builder.build());
    }

    public TenantIdentityProvider createJwt(CreateTenantJwtIdentityProviderRequest request) throws IOException {
        return createProvider("jwt", request);
    }

    public TenantIdentityProvider createJwt(Consumer<CreateTenantJwtIdentityProviderRequest.Builder> configure)
            throws IOException {
        CreateTenantJwtIdentityProviderRequest.Builder builder = CreateTenantJwtIdentityProviderRequest.builder();
        configure.accept(builder);
        return createJwt(builder.build());
    }

    public TenantIdentityProvider createSaml(CreateTenantSamlIdentityProviderRequest request) throws IOException {
        return createProvider("saml", request);
    }

    public TenantIdentityProvider createSaml(Consumer<CreateTenantSamlIdentityProviderRequest.Builder> configure)
            throws IOException {
        CreateTenantSamlIdentityProviderRequest.Builder builder = CreateTenantSamlIdentityProviderRequest.builder();
        configure.accept(builder);
        return createSaml(builder.build());
    }

    public TenantIdentityProvider createLdap(CreateTenantLdapIdentityProviderRequest request) throws IOException {
        return createProvider("ldap", request);
    }

    public TenantIdentityProvider createLdap(Consumer<CreateTenantLdapIdentityProviderRequest.Builder> configure)
            throws IOException {
        CreateTenantLdapIdentityProviderRequest.Builder builder = CreateTenantLdapIdentityProviderRequest.builder();
        configure.accept(builder);
        return createLdap(builder.build());
    }

    public TenantIdentityProvider createGoogle(CreateTenantGoogleIdentityProviderRequest request) throws IOException {
        return createProvider("google", request);
    }

    public TenantIdentityProvider createGoogle(Consumer<CreateTenantGoogleIdentityProviderRequest.Builder> configure)
            throws IOException {
        CreateTenantGoogleIdentityProviderRequest.Builder builder = CreateTenantGoogleIdentityProviderRequest.builder();
        configure.accept(builder);
        return createGoogle(builder.build());
    }

    public TenantIdentityProvider createAzureAd(CreateTenantAzureAdIdentityProviderRequest request)
            throws IOException {
        return createProvider("azure-ad", request);
    }

    public TenantIdentityProvider createAzureAd(Consumer<CreateTenantAzureAdIdentityProviderRequest.Builder> configure)
            throws IOException {
        CreateTenantAzureAdIdentityProviderRequest.Builder builder = CreateTenantAzureAdIdentityProviderRequest.builder();
        configure.accept(builder);
        return createAzureAd(builder.build());
    }

    public TenantIdentityProvider createGitHub(CreateTenantGitHubIdentityProviderRequest request)
            throws IOException {
        return createProvider("github", request);
    }

    public TenantIdentityProvider createGitHub(Consumer<CreateTenantGitHubIdentityProviderRequest.Builder> configure)
            throws IOException {
        CreateTenantGitHubIdentityProviderRequest.Builder builder = CreateTenantGitHubIdentityProviderRequest.builder();
        configure.accept(builder);
        return createGitHub(builder.build());
    }

    public TenantIdentityProvider createGitHubEnterpriseServer(
            CreateTenantGitHubEnterpriseServerIdentityProviderRequest request) throws IOException {
        return createProvider("github-enterprise-server", request);
    }

    public TenantIdentityProvider createGitHubEnterpriseServer(
            Consumer<CreateTenantGitHubEnterpriseServerIdentityProviderRequest.Builder> configure) throws IOException {
        CreateTenantGitHubEnterpriseServerIdentityProviderRequest.Builder builder =
                CreateTenantGitHubEnterpriseServerIdentityProviderRequest.builder();
        configure.accept(builder);
        return createGitHubEnterpriseServer(builder.build());
    }

    public TenantIdentityProvider createGitLab(CreateTenantGitLabIdentityProviderRequest request)
            throws IOException {
        return createProvider("gitlab", request);
    }

    public TenantIdentityProvider createGitLab(Consumer<CreateTenantGitLabIdentityProviderRequest.Builder> configure)
            throws IOException {
        CreateTenantGitLabIdentityProviderRequest.Builder builder = CreateTenantGitLabIdentityProviderRequest.builder();
        configure.accept(builder);
        return createGitLab(builder.build());
    }

    public TenantIdentityProvider createGitLabSelfHosted(
            CreateTenantGitLabSelfHostedIdentityProviderRequest request) throws IOException {
        return createProvider("gitlab-self-hosted", request);
    }

    public TenantIdentityProvider createGitLabSelfHosted(
            Consumer<CreateTenantGitLabSelfHostedIdentityProviderRequest.Builder> configure) throws IOException {
        CreateTenantGitLabSelfHostedIdentityProviderRequest.Builder builder =
                CreateTenantGitLabSelfHostedIdentityProviderRequest.builder();
        configure.accept(builder);
        return createGitLabSelfHosted(builder.build());
    }

    public TenantIdentityProvider createApple(CreateTenantAppleIdentityProviderRequest request) throws IOException {
        return createProvider("apple", request);
    }

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

    public IdentityProvisioning getProvisioning() throws IOException {
        return client.get(provisioningPath()).as(IdentityProvisioning.class);
    }

    /** Replace the tenant's identity-provisioning roles. */
    public IdentityProvisioning updateProvisioning(List<String> roles) throws IOException {
        return updateProvisioning(new UpdateIdentityProvisioningRequest(roles));
    }

    public IdentityProvisioning updateProvisioning(UpdateIdentityProvisioningRequest request) throws IOException {
        return client.put(provisioningPath(), request).as(IdentityProvisioning.class);
    }

    public IdentityProvisioning updateProvisioning(Consumer<UpdateIdentityProvisioningRequest.Builder> configure)
            throws IOException {
        UpdateIdentityProvisioningRequest.Builder builder = UpdateIdentityProvisioningRequest.builder();
        configure.accept(builder);
        return updateProvisioning(builder.build());
    }

    /** Claim workspace access for an identity-provider-provisioned user. */
    public IdentityAccessClaim claimAccess() throws IOException {
        return client.post("/tenants/" + seg(tenantSlug) + "/identity/claim-access", null)
                .as(IdentityAccessClaim.class);
    }

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
