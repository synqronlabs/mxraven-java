package com.mxraven.mail.notify;


/**
 * Typed {@link NotificationHandler} for when you handle specific event kinds.
 * Override only the callbacks you need; the others acknowledge and continue by
 * default.
 *
 * <pre>{@code
 * NotificationHandler handler = new NotificationCallbacks() {
 *     @Override
 *     public HandlerResult onAccepted(AcceptedMessage message) {
 *         app.processInbound(email);
 *         return HandlerResult.CONTINUE;
 *     }
 *
 *     @Override
 *     public HandlerResult onAttempt(AttemptEvent event) {
 *         app.recordAttempt(event);
 *         return HandlerResult.CONTINUE;
 *     }
 * };
 * }</pre>
 *
 * <p>Prefer the functional {@link NotificationHandler} when a single lambda is
 * enough:
 *
 * <pre>{@code
 * NotificationHandler handler = event -> HandlerResult.CONTINUE;
 * }</pre>
 *
 * <p>Because this extends {@link NotificationHandler}, a
 * {@code NotificationCallbacks} can be passed anywhere a handler is accepted.
 */
public interface NotificationCallbacks extends NotificationHandler {

    /** Dispatches to the typed callback for the event. */
    @Override
    default HandlerResult handle(NotificationEvent event) throws Exception {
        if (event instanceof AcceptedNotification notification) {
            return onAccepted(notification.message());
        }
        if (event instanceof AttemptNotification attempt) {
            return onAttempt(attempt.attempt());
        }
        if (event instanceof FaultNotification fault) {
            return onFault(fault.fault());
        }
        throw new NotificationException("notify: unsupported event kind " + event.kind());
    }

    /**
     * Handles a message the listener accepted. Acknowledges and continues by
     * default.
     *
     * @param message the accepted message and its routing-script decision
     * @return whether consumption should continue or stop
     * @throws Exception when the event cannot be processed; the event is
     *                   negatively acknowledged for redelivery
     */
    default HandlerResult onAccepted(AcceptedMessage message) throws Exception {
        return HandlerResult.CONTINUE;
    }

    /**
     * Handles one delivery attempt. Acknowledges and continues by default.
     * Branch on {@link AttemptEvent#channel()} to distinguish the
     * {@code smtp} and {@code s3} egress workers.
     *
     * @param event the attempt details
     * @return whether consumption should continue or stop
     * @throws Exception when the event cannot be processed; the event is
     *                   negatively acknowledged for redelivery
     */
    default HandlerResult onAttempt(AttemptEvent event) throws Exception {
        return HandlerResult.CONTINUE;
    }

    /**
     * Handles a sampled MTA routing-script fault. Acknowledges and continues by
     * default.
     *
     * @param event the fault details
     * @return whether consumption should continue or stop
     * @throws Exception when the event cannot be processed; the event is
     *                   negatively acknowledged for redelivery
     */
    default HandlerResult onFault(FaultEvent event) throws Exception {
        return HandlerResult.CONTINUE;
    }
}
