package com.mxraven.admin;

import com.mxraven.admin.client.AutoReplyTemplatesClient;
import com.mxraven.admin.client.DomainsClient;
import com.mxraven.admin.client.GovernanceClient;
import com.mxraven.admin.client.IdentityProvidersClient;
import com.mxraven.admin.client.InboundRoutesClient;
import com.mxraven.admin.client.ListenersClient;
import com.mxraven.admin.client.MailAnalyticsClient;
import com.mxraven.admin.client.MtaRateLimitsClient;
import com.mxraven.admin.client.QuotasClient;
import com.mxraven.admin.client.RecipientSetsClient;
import com.mxraven.admin.client.SmtpForwardDestinationsClient;
import com.mxraven.admin.client.SmtpRelaysClient;
import com.mxraven.admin.client.StorageIntegrationsClient;
import com.mxraven.admin.client.SuppressionsClient;
import com.mxraven.admin.client.WebhookEndpointsClient;
import com.mxraven.admin.model.Tenant;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * A tenant-bound view of the control plane. Obtain one from
 * {@link AdminClient#workspace(String)} and reach every tenant-scoped resource
 * family from it, without repeating the tenant slug on every call:
 *
 * <pre>{@code
 * Workspace ws = admin.workspace("my-workspace");
 * for (Domain domain : ws.domains().list()) {
 *     System.out.println(domain.domainName() + " " + domain.status());
 * }
 * }</pre>
 *
 * <p>The public {@code auth()} family stays on {@link AdminClient}.
 */
public final class Workspace {
    private final AdminClient client;
    private final String tenantSlug;

    Workspace(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    public String slug() {
        return tenantSlug;
    }

    /** Fetch this workspace's identity and provisioning state. */
    public Tenant tenant() throws IOException {
        return client.get("/tenants/" + encode(tenantSlug)).as(Tenant.class);
    }

    public DomainsClient domains() {
        return new DomainsClient(client, tenantSlug);
    }

    public ListenersClient listeners() {
        return new ListenersClient(client, tenantSlug);
    }

    public SuppressionsClient suppressions() {
        return new SuppressionsClient(client, tenantSlug);
    }

    public RecipientSetsClient recipientSets() {
        return new RecipientSetsClient(client, tenantSlug);
    }

    public AutoReplyTemplatesClient autoReplyTemplates() {
        return new AutoReplyTemplatesClient(client, tenantSlug);
    }

    public SmtpForwardDestinationsClient smtpForwardDestinations() {
        return new SmtpForwardDestinationsClient(client, tenantSlug);
    }

    public InboundRoutesClient inboundRoutes() {
        return new InboundRoutesClient(client, tenantSlug);
    }

    public SmtpRelaysClient smtpRelays() {
        return new SmtpRelaysClient(client, tenantSlug);
    }

    public StorageIntegrationsClient storageIntegrations() {
        return new StorageIntegrationsClient(client, tenantSlug);
    }

    public WebhookEndpointsClient webhookEndpoints() {
        return new WebhookEndpointsClient(client, tenantSlug);
    }

    public IdentityProvidersClient identityProviders() {
        return new IdentityProvidersClient(client, tenantSlug);
    }

    public QuotasClient quotas() {
        return new QuotasClient(client, tenantSlug);
    }

    public MtaRateLimitsClient mtaRateLimits() {
        return new MtaRateLimitsClient(client, tenantSlug);
    }

    public GovernanceClient governance() {
        return new GovernanceClient(client, tenantSlug);
    }

    public MailAnalyticsClient mailAnalytics() {
        return new MailAnalyticsClient(client, tenantSlug);
    }

    private static String encode(String segment) {
        return URLEncoder.encode(segment, StandardCharsets.UTF_8);
    }
}
