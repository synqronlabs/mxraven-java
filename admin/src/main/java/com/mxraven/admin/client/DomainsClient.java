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
 * <pre>{@code
 * for (Domain d : ws.domains().list()) {
 *     System.out.println(d.domainName() + " " + d.status());
 * }
 *
 * Domain d = ws.domains().create("example.com");
 * d.replace("dmarc@example.com");
 * }</pre>
 */
public final class DomainsClient {
    private final AdminClient client;
    private final String tenantSlug;

    public DomainsClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    /** Start a typed, fluent list request. */
    public DomainQuery query() {
        return new DomainQuery(this);
    }

    /** List all domains (first page fetched eagerly, remaining pages lazy). */
    public Paged<Domain> list() throws IOException {
        return list(null);
    }

    Paged<Domain> list(QueryParams params) throws IOException {
        return client.paged(path(), params == null ? null : params.toMap(), DomainData.class)
                .map(data -> new Domain(client, path() + "/" + data.id(), data));
    }

    public Domain get(String domainId) throws IOException {
        String resourcePath = path() + "/" + domainId;
        return new Domain(client, resourcePath, client.get(resourcePath).as(DomainData.class));
    }

    /** Onboard a domain for outbound sending. */
    public Domain create(String domainName) throws IOException {
        return create(domainName, null);
    }

    /** Onboard a domain and set its DMARC aggregate report address. */
    public Domain create(String domainName, String dmarcReportAddress) throws IOException {
        DomainData data = client.post(path(), new CreateDomainRequest(domainName, dmarcReportAddress))
                .as(DomainData.class);
        return new Domain(client, path() + "/" + data.id(), data);
    }

    private String path() {
        return "/tenants/" + tenantSlug + "/domains";
    }
}
