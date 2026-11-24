package com.mxraven.admin.model;

/**
 * Request body for
 * {@code PUT /v2/tenants/{slug}/listeners/{id}/script/body-access}.
 *
 * @param enabled whether {@code mail.body:text()} may read the spooled body
 */
public record UpdateListenerScriptBodyAccessRequest(boolean enabled) {
}
