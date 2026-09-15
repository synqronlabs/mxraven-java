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

    public AuditLogQuery search(String query) {
        params.q(query);
        return this;
    }

    public AuditLogQuery actorKind(AuditActorKind actorKind) {
        params.put("actor_kind", actorKind == null ? null : actorKind.wire());
        return this;
    }

    public AuditLogQuery actorId(String actorId) {
        params.put("actor_id", actorId);
        return this;
    }

    public AuditLogQuery action(String action) {
        params.put("action", action);
        return this;
    }

    public AuditLogQuery resourceType(String resourceType) {
        params.put("resource_type", resourceType);
        return this;
    }

    public AuditLogQuery status(AuditStatus status) {
        params.put("status", status == null ? null : status.wire());
        return this;
    }

    /** Inclusive lower bound as an ISO-8601 timestamp. */
    public AuditLogQuery createdFrom(String createdFrom) {
        params.put("created_from", createdFrom);
        return this;
    }

    /** Exclusive upper bound as an ISO-8601 timestamp. */
    public AuditLogQuery createdTo(String createdTo) {
        params.put("created_to", createdTo);
        return this;
    }

    public AuditLogQuery pageSize(int pageSize) {
        params.pageSize(pageSize);
        return this;
    }

    public AuditLogQuery pageToken(String pageToken) {
        params.pageToken(pageToken);
        return this;
    }

    public Paged<AuditLog> list() throws IOException {
        return client.listAuditLog(params);
    }
}
