/**
 * Consumes mxRaven client notification events over NATS JetStream.
 *
 * <p>When notifications are enabled for a listener (see the {@code admin}
 * module's listener notification setting), mxRaven publishes every accepted
 * message and every delivery attempt to that listener's durable JetStream
 * consumer. This package consumes those events with the listener's ordinary
 * API key:
 *
 * <pre>{@code
 * NotificationClient client = NotificationClient.connect(
 *         NotificationClientOptions.builder()
 *                 .username("mxr_tx_ab12cd34ef56")
 *                 .secret(System.getenv("MXRAVEN_SECRET"))
 *                 .listener(listenerId)
 *                 .build());
 *
 * client.consume(new NotificationCallbacks() {
 *     @Override
 *     public HandlerResult onAttempt(AttemptEvent event) {
 *         System.out.println(event.status());
 *         return HandlerResult.CONTINUE;
 *     }
 * });
 * }</pre>
 *
 * <p>{@link com.mxraven.mail.notify.NotificationClient#consume} acknowledges an
 * event when its handler returns, redelivers it with exponential backoff when
 * the handler throws, and stops when the handler returns
 * {@link com.mxraven.mail.notify.HandlerResult#STOP}.
 * {@link com.mxraven.mail.notify.NotificationClient#events()} is the
 * lower-level iterator for callers that acknowledge each event themselves.
 *
 * <p>Accepted events reuse the webhook payload model,
 * {@link com.mxraven.mail.webhook.InboundEmail}; an MTA listener's accepted
 * event carries raw-message access that
 * {@link com.mxraven.mail.webhook.InboundEmail#parse()} downloads. Attempt
 * events are described by {@link com.mxraven.mail.notify.AttemptEvent}.
 *
 * <p>The consumer and its subject are managed by the control plane. Enable
 * notifications with the administrative API before connecting; the consumer
 * name defaults to {@code notify-<listener>}.
 */
package com.mxraven.mail.notify;
