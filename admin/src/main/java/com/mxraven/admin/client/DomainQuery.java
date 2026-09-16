package com.mxraven.admin.client;

import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.Domain;
import com.mxraven.admin.model.DomainStatus;

import java.io.IOException;

/**
 * Typed filter and pagination builder for {@link DomainsClient#list()}. Filters
 * are combined with AND; results are ordered by case-insensitive domain name.
 *
 * <pre>
 * Paged&lt;Domain&gt; verified = ws.domains().query()
 *         .status(DomainStatus.VERIFIED)
 *         .dkimVerified(true)
 *         .search("example.com")
 *         .list();
 * </pre>
 *
 * <p>Obtain one from {@link DomainsClient#query()}. Instances are not thread-safe.
 */
public final class DomainQuery {
    private final DomainsClient client;
    private final QueryParams params = QueryParams.create();

    DomainQuery(DomainsClient client) {
        this.client = client;
    }

    /**
     * Case-insensitive search across the resource.
     *
     * @param query search text
     * @return this query
     */
    public DomainQuery search(String query) {
        params.q(query);
        return this;
    }

    /**
     * Restrict to a single lifecycle status.
     *
     * @param status domain status; ignored when {@code null}
     * @return this query
     */
    public DomainQuery status(DomainStatus status) {
        params.put("status", status == null ? null : status.wire());
        return this;
    }

    /**
     * Filter by whether SPF verification passed.
     *
     * @param verified required SPF verification state
     * @return this query
     */
    public DomainQuery spfVerified(boolean verified) {
        params.put("spf_verified", verified);
        return this;
    }

    /**
     * Filter by whether DKIM verification passed.
     *
     * @param verified required DKIM verification state
     * @return this query
     */
    public DomainQuery dkimVerified(boolean verified) {
        params.put("dkim_verified", verified);
        return this;
    }

    /**
     * Filter by whether DMARC verification passed.
     *
     * @param verified required DMARC verification state
     * @return this query
     */
    public DomainQuery dmarcVerified(boolean verified) {
        params.put("dmarc_verified", verified);
        return this;
    }

    /**
     * Filter by whether outbound sending setup is enabled.
     *
     * @param enabled required sending-enabled state
     * @return this query
     */
    public DomainQuery sendingEnabled(boolean enabled) {
        params.put("sending_enabled", enabled);
        return this;
    }

    /**
     * Maximum resources per page ({@code 1..500}, server default {@code 100}).
     *
     * @param pageSize page size, between 1 and 500
     * @return this query
     */
    public DomainQuery pageSize(int pageSize) {
        params.pageSize(pageSize);
        return this;
    }

    /**
     * Continue from a previously returned page.
     *
     * @param pageToken opaque page token; {@code null} is ignored
     * @return this query
     */
    public DomainQuery pageToken(String pageToken) {
        params.pageToken(pageToken);
        return this;
    }

    /**
     * Fetch the matching domains (first page fetched eagerly, remaining pages
     * lazy).
     *
     * @return a lazily paginating collection of domains
     * @throws IOException if the first page request fails or is interrupted
     */
    public Paged<Domain> list() throws IOException {
        return client.list(params);
    }
}
