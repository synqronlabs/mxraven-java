/**
 * Consumes mxRaven client notification events over NATS JetStream.
 *
 * <p>When notifications are enabled for a listener (see the {@code admin}
 * module's listener notification setting), mxRaven publishes every accepted
 * message and every delivery attempt to that listener's durable JetStream
 * consumer. This package consumes those events with the listener's ordinary
 * API key:
 *
 * <pre>
 * NotificationClient client = NotificationClient.connect(
 *         NotificationClientOptions.builder()
 *                 .username("mxr_tx_ab12cd34ef56")
 *                 .secret(System.getenv("MXRAVEN_SECRET"))
 *                 .listener(listenerId)
 *                 .build());
 *
 * client.consume(new NotificationCallbacks() {
 *     &#64;Override
 *     public HandlerResult onAttempt(AttemptEvent event) {
 *         System.out.println(event.status());
 *         return HandlerResult.CONTINUE;
 *     }
 * });
 * </pre>
 *
 * <p>{@link com.mxraven.mail.notify.NotificationClient#consume} acknowledges an
 * event when its handler returns, redelivers it with exponential backoff when
 * the handler throws, and stops when the handler returns
 * {@link com.mxraven.mail.notify.HandlerResult#STOP}.
 * {@link com.mxraven.mail.notify.NotificationClient#events()} is the
 * lower-level iterator for callers that acknowledge each event themselves.
 *
 * <p>Fault events are described by {@link com.mxraven.mail.notify.FaultEvent}: an
 * MTA listener's routing script faulted and deferred the transaction, so there
 * is no accepted event. They are sampled per script revision.
 *
 * <p>Accepted events are described by {@link com.mxraven.mail.notify.AcceptedMessage}:
 * the message summary and headers, the SMTP envelope, the routing-script
 * decision, and raw-message access. An MTA listener's accepted event carries a
 * {@link com.mxraven.mail.notify.RawEmail} that
 * {@link com.mxraven.mail.notify.AcceptedMessage#parse()} downloads; a
 * submission listener's event leaves it empty. Attempt events are described by
 * {@link com.mxraven.mail.notify.AttemptEvent}.
 *
 * <p>The consumer and its subject are managed by the control plane. Enable
 * notifications with the administrative API before connecting; the consumer
 * name defaults to {@code notify-<listener>}.
 */
package com.mxraven.mail.notify;
