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
 * <pre>{@code
 * Paged<Domain> verified = ws.domains().query()
 *         .status(DomainStatus.VERIFIED)
 *         .dkimVerified(true)
 *         .search("example.com")
 *         .list();
 * }</pre>
 *
 * <p>Obtain one from {@link DomainsClient#query()}. Instances are not thread-safe.
 */
public final class DomainQuery {
    private final DomainsClient client;
    private final QueryParams params = QueryParams.create();

    DomainQuery(DomainsClient client) {
        this.client = client;
    }

    /** Case-insensitive search across the resource. */
    public DomainQuery search(String query) {
        params.q(query);
        return this;
    }

    /** Restrict to a single lifecycle status. */
    public DomainQuery status(DomainStatus status) {
        params.put("status", status == null ? null : status.wire());
        return this;
    }

    public DomainQuery spfVerified(boolean verified) {
        params.put("spf_verified", verified);
        return this;
    }

    public DomainQuery dkimVerified(boolean verified) {
        params.put("dkim_verified", verified);
        return this;
    }

    public DomainQuery dmarcVerified(boolean verified) {
        params.put("dmarc_verified", verified);
        return this;
    }

    /** Filter by whether outbound sending setup is enabled. */
    public DomainQuery sendingEnabled(boolean enabled) {
        params.put("sending_enabled", enabled);
        return this;
    }

    /** Maximum resources per page ({@code 1..500}, server default {@code 100}). */
    public DomainQuery pageSize(int pageSize) {
        params.pageSize(pageSize);
        return this;
    }

    public DomainQuery pageToken(String pageToken) {
        params.pageToken(pageToken);
        return this;
    }

    public Paged<Domain> list() throws IOException {
        return client.list(params);
    }
}
