package com.mxraven.admin.client;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.AuditLog;
import com.mxraven.admin.model.TenantDedicatedIPPool;
import com.mxraven.admin.model.TenantResourceSearchResult;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Tenant-scoped governance reads: audit log, dedicated IP pools, and resource
 * search.
 *
 * <p>All operations are tenant-bound and require the tenant slug.
 */
public final class GovernanceClient {
    private final AdminClient client;
    private final String tenantSlug;

    public GovernanceClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    /** Start a typed, fluent audit-log query. */
    public AuditLogQuery auditQuery() {
        return new AuditLogQuery(this);
    }

    /** Start a typed resource search. */
    public ResourceSearchQuery searchQuery() {
        return new ResourceSearchQuery(this);
    }

    public Paged<AuditLog> listAuditLog() throws IOException {
        return listAuditLog(null);
    }

    Paged<AuditLog> listAuditLog(QueryParams params) throws IOException {
        return client.paged(tenantPath("/audit-log"), params == null ? null : params.toMap(), AuditLog.class);
    }

    public Paged<TenantDedicatedIPPool> listDedicatedIPPools() throws IOException {
        return listDedicatedIPPools(null);
    }

    /**
     * @param params optional {@code page_size} and {@code page_token} filters
     */
    public Paged<TenantDedicatedIPPool> listDedicatedIPPools(QueryParams params)
            throws IOException {
        return client.paged(tenantPath("/dedicated-ip-pools"), params == null ? null : params.toMap(), TenantDedicatedIPPool.class);
    }

    /** Search across tenant resources by a literal term. */
    public Paged<TenantResourceSearchResult> search(String query) throws IOException {
        return search(QueryParams.create().q(query));
    }

    Paged<TenantResourceSearchResult> search(QueryParams params) throws IOException {
        return client.paged(tenantPath("/search"), params == null ? null : params.toMap(), TenantResourceSearchResult.class);
    }

    private String tenantPath(String suffix) {
        return "/tenants/" + encode(tenantSlug) + suffix;
    }

    private String encode(String segment) {
        return URLEncoder.encode(segment, StandardCharsets.UTF_8);
    }
}
