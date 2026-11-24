package com.mxraven.admin.model;

/**
 * A grantable connected-application permission.
 *
 * @param scope the scope identifier
 * @param description customer-facing explanation shown before approval
 */
public record ConnectedAppScope(String scope, String description) {
}
