/**
 * Resource-family clients for the mxRaven control-plane v2 REST API.
 *
 * <p>These types are not constructed directly. Tenant-scoped families are
 * obtained from {@link com.mxraven.admin.Workspace} (for example
 * {@link DomainsClient}, {@link ListenersClient},
 * {@link IdentityProvidersClient}, {@link MailAnalyticsClient}, and
 * {@link GovernanceClient}), and the public authentication family from
 * {@link com.mxraven.admin.AdminClient#auth()} via {@link AuthClient}.
 *
 * <p>Collection clients return hydrated entities wrapped in
 * {@link com.mxraven.admin.Paged} and expose typed fluent query builders such as
 * {@link DomainQuery}, {@link ListenerQuery}, {@link InboundRouteQuery}, and
 * {@link AuditLogQuery}. Single-listener children such as {@link ApiKeysClient}
 * and {@link ListenerMtaRateLimitClient} are reached from their owning entity.
 */
package com.mxraven.admin.client;
