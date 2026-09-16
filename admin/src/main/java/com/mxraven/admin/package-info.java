/**
 * Core client types for the mxRaven control-plane v2 REST API.
 *
 * <p>The entry point is {@link AdminClient}, configured with a control-plane
 * base URL and a ZITADEL-issued bearer token. Tenant-scoped resource families
 * are reached through a {@link Workspace} bound to a tenant slug, and public
 * authentication resources stay on {@link AdminClient}. Successful responses
 * are wrapped in {@link Response}; errors are raised as
 * {@link com.mxraven.admin.exception.ApiException}.
 */
package com.mxraven.admin;
