package com.mxraven.admin.client;

import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.AuditActorKind;
import com.mxraven.admin.model.AuditLog;
import com.mxraven.admin.model.AuditStatus;

import java.io.IOException;

/**
 * Typed filter builder for {@link GovernanceClient#auditQuery()}.
 */
public final class AuditLogQuery {
    private final GovernanceClient client;
    private final QueryParams params = QueryParams.create();

    AuditLogQuery(GovernanceClient client) {
        this.client = client;
    }

    /**
     * Set the {@code q} search filter.
     *
     * @param query search text
     * @return this query
     */
    public AuditLogQuery search(String query) {
        params.q(query);
        return this;
    }

    /**
     * Filter by actor kind.
     *
     * @param actorKind actor kind; ignored when {@code null}
     * @return this query
     */
    public AuditLogQuery actorKind(AuditActorKind actorKind) {
        params.put("actor_kind", actorKind == null ? null : actorKind.wire());
        return this;
    }

    /**
     * Filter by actor identifier.
     *
     * @param actorId actor identifier
     * @return this query
     */
    public AuditLogQuery actorId(String actorId) {
        params.put("actor_id", actorId);
        return this;
    }

    /**
     * Filter by action.
     *
     * @param action action name
     * @return this query
     */
    public AuditLogQuery action(String action) {
        params.put("action", action);
        return this;
    }

    /**
     * Filter by resource type.
     *
     * @param resourceType resource type
     * @return this query
     */
    public AuditLogQuery resourceType(String resourceType) {
        params.put("resource_type", resourceType);
        return this;
    }

    /**
     * Filter by audit status.
     *
     * @param status audit status; ignored when {@code null}
     * @return this query
     */
    public AuditLogQuery status(AuditStatus status) {
        params.put("status", status == null ? null : status.wire());
        return this;
    }

    /**
     * Inclusive lower bound as an ISO-8601 timestamp.
     *
     * @param createdFrom inclusive {@code created_from} bound
     * @return this query
     */
    public AuditLogQuery createdFrom(String createdFrom) {
        params.put("created_from", createdFrom);
        return this;
    }

    /**
     * Exclusive upper bound as an ISO-8601 timestamp.
     *
     * @param createdTo exclusive {@code created_to} bound
     * @return this query
     */
    public AuditLogQuery createdTo(String createdTo) {
        params.put("created_to", createdTo);
        return this;
    }

    /**
     * Set the maximum number of audit-log entries per page.
     *
     * @param pageSize page size, between 1 and 500
     * @return this query
     */
    public AuditLogQuery pageSize(int pageSize) {
        params.pageSize(pageSize);
        return this;
    }

    /**
     * Continue from a previously returned page.
     *
     * @param pageToken opaque page token; {@code null} is ignored
     * @return this query
     */
    public AuditLogQuery pageToken(String pageToken) {
        params.pageToken(pageToken);
        return this;
    }

    /**
     * Fetch the matching audit-log entries (first page fetched eagerly, remaining
     * pages lazy).
     *
     * @return a lazily paginating collection of audit-log entries
     * @throws IOException if the first page request fails or is interrupted
     */
    public Paged<AuditLog> list() throws IOException {
        return client.listAuditLog(params);
    }
}
