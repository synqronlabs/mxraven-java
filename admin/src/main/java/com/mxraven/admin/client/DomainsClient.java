package com.mxraven.admin.client;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.CreateDomainRequest;
import com.mxraven.admin.model.Domain;
import com.mxraven.admin.model.DomainData;

import java.io.IOException;

/**
 * Tenant domain collection. Every read and creation returns a hydrated
 * {@link Domain} entity whose operations ({@code replace}, {@code delete},
 * {@code listenerGrants}) act on that domain.
 *
 * <pre>
 * for (Domain d : ws.domains().list()) {
 *     System.out.println(d.domainName() + " " + d.status());
 * }
 *
 * Domain d = ws.domains().create("example.com");
 * d.replace("dmarc@example.com");
 * </pre>
 */
public final class DomainsClient {
    private final AdminClient client;
    private final String tenantSlug;

    /**
     * Create a client for the domain collection of a tenant.
     *
     * @param client underlying admin client
     * @param tenantSlug tenant slug whose domains are accessed
     */
    public DomainsClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    /**
     * Start a typed, fluent list request.
     *
     * @return a new domain query
     */
    public DomainQuery query() {
        return new DomainQuery(this);
    }

    /**
     * List all domains (first page fetched eagerly, remaining pages lazy).
     *
     * @return a lazily paginating collection of domains
     * @throws IOException if the first page request fails or is interrupted
     */
    public Paged<Domain> list() throws IOException {
        return list(null);
    }

    Paged<Domain> list(QueryParams params) throws IOException {
        return client.paged(path(), params == null ? null : params.toMap(), DomainData.class)
                .map(data -> new Domain(client, path() + "/" + data.id(), data));
    }

    /**
     * Get a domain by its identifier.
     *
     * @param domainId domain identifier
     * @return the hydrated domain
     * @throws IOException if the request fails or is interrupted
     */
    public Domain get(String domainId) throws IOException {
        String resourcePath = path() + "/" + domainId;
        return new Domain(client, resourcePath, client.get(resourcePath).as(DomainData.class));
    }

    /**
     * Onboard a domain for outbound sending.
     *
     * @param domainName domain name to onboard
     * @return the created domain
     * @throws IOException if the request fails or is interrupted
     */
    public Domain create(String domainName) throws IOException {
        return create(domainName, null);
    }

    /**
     * Onboard a domain and set its DMARC aggregate report address.
     *
     * @param domainName domain name to onboard
     * @param dmarcReportAddress DMARC aggregate report address; may be {@code null}
     * @return the created domain
     * @throws IOException if the request fails or is interrupted
     */
    public Domain create(String domainName, String dmarcReportAddress) throws IOException {
        DomainData data = client.post(path(), new CreateDomainRequest(domainName, dmarcReportAddress))
                .as(DomainData.class);
        return new Domain(client, path() + "/" + data.id(), data);
    }

    private String path() {
        return "/tenants/" + tenantSlug + "/domains";
    }
}
