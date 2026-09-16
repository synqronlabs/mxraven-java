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

    /**
     * Returns the tenant slug this workspace is bound to.
     *
     * @return the tenant slug
     */
    public String slug() {
        return tenantSlug;
    }

    /**
     * Fetch this workspace's identity and provisioning state.
     *
     * @return the tenant record
     * @throws IOException if the request fails or is interrupted
     */
    public Tenant tenant() throws IOException {
        return client.get("/tenants/" + encode(tenantSlug)).as(Tenant.class);
    }

    /**
     * Accesses the domain resource family.
     *
     * @return a client for domain operations in this workspace
     */
    public DomainsClient domains() {
        return new DomainsClient(client, tenantSlug);
    }

    /**
     * Accesses the listener resource family.
     *
     * @return a client for listener operations in this workspace
     */
    public ListenersClient listeners() {
        return new ListenersClient(client, tenantSlug);
    }

    /**
     * Accesses the suppression resource family.
     *
     * @return a client for suppression operations in this workspace
     */
    public SuppressionsClient suppressions() {
        return new SuppressionsClient(client, tenantSlug);
    }

    /**
     * Accesses the recipient-set resource family.
     *
     * @return a client for recipient-set operations in this workspace
     */
    public RecipientSetsClient recipientSets() {
        return new RecipientSetsClient(client, tenantSlug);
    }

    /**
     * Accesses the auto-reply-template resource family.
     *
     * @return a client for auto-reply-template operations in this workspace
     */
    public AutoReplyTemplatesClient autoReplyTemplates() {
        return new AutoReplyTemplatesClient(client, tenantSlug);
    }

    /**
     * Accesses the SMTP forward-destination resource family.
     *
     * @return a client for SMTP forward-destination operations in this workspace
     */
    public SmtpForwardDestinationsClient smtpForwardDestinations() {
        return new SmtpForwardDestinationsClient(client, tenantSlug);
    }

    /**
     * Accesses the inbound-route resource family.
     *
     * @return a client for inbound-route operations in this workspace
     */
    public InboundRoutesClient inboundRoutes() {
        return new InboundRoutesClient(client, tenantSlug);
    }

    /**
     * Accesses the SMTP relay resource family.
     *
     * @return a client for SMTP relay operations in this workspace
     */
    public SmtpRelaysClient smtpRelays() {
        return new SmtpRelaysClient(client, tenantSlug);
    }

    /**
     * Accesses the storage-integration resource family.
     *
     * @return a client for storage-integration operations in this workspace
     */
    public StorageIntegrationsClient storageIntegrations() {
        return new StorageIntegrationsClient(client, tenantSlug);
    }

    /**
     * Accesses the webhook-endpoint resource family.
     *
     * @return a client for webhook-endpoint operations in this workspace
     */
    public WebhookEndpointsClient webhookEndpoints() {
        return new WebhookEndpointsClient(client, tenantSlug);
    }

    /**
     * Accesses the identity-provider resource family.
     *
     * @return a client for identity-provider operations in this workspace
     */
    public IdentityProvidersClient identityProviders() {
        return new IdentityProvidersClient(client, tenantSlug);
    }

    /**
     * Accesses the quota resource family.
     *
     * @return a client for quota operations in this workspace
     */
    public QuotasClient quotas() {
        return new QuotasClient(client, tenantSlug);
    }

    /**
     * Accesses the MTA rate-limit resource family.
     *
     * @return a client for MTA rate-limit operations in this workspace
     */
    public MtaRateLimitsClient mtaRateLimits() {
        return new MtaRateLimitsClient(client, tenantSlug);
    }

    /**
     * Accesses the governance resource family.
     *
     * @return a client for governance operations in this workspace
     */
    public GovernanceClient governance() {
        return new GovernanceClient(client, tenantSlug);
    }

    /**
     * Accesses the mail-analytics resource family.
     *
     * @return a client for mail-analytics operations in this workspace
     */
    public MailAnalyticsClient mailAnalytics() {
        return new MailAnalyticsClient(client, tenantSlug);
    }

    private static String encode(String segment) {
        return URLEncoder.encode(segment, StandardCharsets.UTF_8);
    }
}
