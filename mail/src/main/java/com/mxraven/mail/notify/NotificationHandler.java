package com.mxraven.mail.notify;

/**
 * Processes one notification event.
 *
 * <p>Returning {@link HandlerResult#CONTINUE} acknowledges the event.
 * Returning {@link HandlerResult#STOP} acknowledges the event and stops
 * consumption. Throwing leaves the event unacknowledged, and the server
 * redelivers it after a delay that doubles with each attempt up to 30 seconds.
 * Events are handled one at a time, so a slow handler applies backpressure to
 * the pull.
 *
 * <p>Prefer {@link NotificationCallbacks} when you handle specific event kinds
 * with typed callbacks instead of branching on the event class.
 */
public interface NotificationHandler {
    /**
     * Handles one event.
     *
     * @param event the decoded notification
     * @return whether consumption should continue or stop
     * @throws Exception when the event cannot be processed; the event is
     *                   negatively acknowledged for redelivery
     */
    HandlerResult handle(NotificationEvent event) throws Exception;
}
