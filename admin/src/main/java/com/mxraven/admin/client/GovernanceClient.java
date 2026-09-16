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

    /**
     * Create a client for the tenant-scoped governance reads.
     *
     * @param client underlying admin client
     * @param tenantSlug tenant slug whose governance data is read
     */
    public GovernanceClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    /**
     * Start a typed, fluent audit-log query.
     *
     * @return a new audit-log query
     */
    public AuditLogQuery auditQuery() {
        return new AuditLogQuery(this);
    }

    /**
     * Start a typed resource search.
     *
     * @return a new resource search query
     */
    public ResourceSearchQuery searchQuery() {
        return new ResourceSearchQuery(this);
    }

    /**
     * List the tenant audit log (first page fetched eagerly, remaining pages
     * lazy).
     *
     * @return a lazily paginating collection of audit-log entries
     * @throws IOException if the first page request fails or is interrupted
     */
    public Paged<AuditLog> listAuditLog() throws IOException {
        return listAuditLog(null);
    }

    Paged<AuditLog> listAuditLog(QueryParams params) throws IOException {
        return client.paged(tenantPath("/audit-log"), params == null ? null : params.toMap(), AuditLog.class);
    }

    /**
     * List the tenant dedicated IP pools (first page fetched eagerly, remaining
     * pages lazy).
     *
     * @return a lazily paginating collection of dedicated IP pools
     * @throws IOException if the first page request fails or is interrupted
     */
    public Paged<TenantDedicatedIPPool> listDedicatedIPPools() throws IOException {
        return listDedicatedIPPools(null);
    }

    /**
     * List the tenant dedicated IP pools (first page fetched eagerly, remaining
     * pages lazy).
     *
     * @param params optional {@code page_size} and {@code page_token} filters
     * @return a lazily paginating collection of dedicated IP pools
     * @throws IOException if the first page request fails or is interrupted
     */
    public Paged<TenantDedicatedIPPool> listDedicatedIPPools(QueryParams params)
            throws IOException {
        return client.paged(tenantPath("/dedicated-ip-pools"), params == null ? null : params.toMap(), TenantDedicatedIPPool.class);
    }

    /**
     * Search across tenant resources by a literal term.
     *
     * @param query literal search term
     * @return a lazily paginating collection of matching resources
     * @throws IOException if the first page request fails or is interrupted
     */
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
