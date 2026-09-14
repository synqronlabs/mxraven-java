/**
 * Verifies and decodes mxRaven webhook deliveries.
 *
 * <p>mxRaven signs every webhook delivery with HMAC-SHA256 over a canonical
 * request string and sends the signature in the {@code X-MxRaven-*} headers.
 * A server that exposes a webhook endpoint verifies the request with a
 * {@link com.mxraven.mail.webhook.WebhookVerifier} and decodes the JSON body with
 * {@link com.mxraven.mail.webhook.WebhookEvent#decode(byte[])}.
 *
 * <p>{@link com.mxraven.mail.webhook.WebhookHandler} bundles verification,
 * idempotency, and dispatch into one framework-agnostic handler. Adapt any HTTP
 * stack to it, or let {@link com.mxraven.mail.webhook.WebhookServer} serve it
 * over the JDK HTTP server.
 *
 * <p>Two webhook actions are supported:
 * <ul>
 *   <li>{@code DELIVER_WEBHOOK} &mdash; a durable delivery carrying a complete
 *       inbound message ({@link com.mxraven.mail.webhook.InboundEmail}).</li>
 *   <li>{@code NOTIFY_WEBHOOK} &mdash; a best-effort delivery-status callback from
 *       the SMTP or object-storage workers.</li>
 * </ul>
 */
package com.mxraven.mail.webhook;
