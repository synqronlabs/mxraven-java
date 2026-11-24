/**
 * Verifies signed mxRaven endpoint calls.
 *
 * <p>Routing scripts call tenant-registered endpoints with {@code mx.http}, and
 * mxRaven signs each request with the endpoint's HMAC-SHA256 key. The signature
 * covers the method, host, request target, timestamp, request ID, and raw body,
 * and is sent in the {@code X-MxRaven-*} headers. A receiver verifies the
 * request with {@link com.mxraven.mail.webhook.WebhookVerifier} and then parses
 * the payload its endpoint defines:
 *
 * <pre>{@code
 * verifier.verify(method, requestUri, headers, body);
 * MyPayload payload = json.readValue(body, MyPayload.class);
 * }</pre>
 *
 * <p>The {@code X-MxRaven-Listener} and {@code X-MxRaven-Revision} headers
 * identify the script call but are not signed; use them as context, not for
 * authorization.
 *
 * <p>An endpoint configured with a stored credential is called with that
 * credential header and no {@code X-MxRaven-*} signature headers. Those calls
 * are not verifiable with this package; validate the credential with your own
 * authentication middleware.
 *
 * <p>Webhook deliveries no longer exist. Accepted messages, delivery attempts,
 * and sampled routing-script faults arrive on the client notification stream in
 * {@link com.mxraven.mail.notify}.
 */
package com.mxraven.mail.webhook;
