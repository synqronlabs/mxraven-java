package com.mxraven.admin.model;

/**
 * Request body for {@code POST /v2/tenants/{slug}/listeners/{id}/script/rollback}.
 *
 * @param revision previously published revision to make live again
 */
public record RollbackListenerScriptRequest(int revision) {
}
