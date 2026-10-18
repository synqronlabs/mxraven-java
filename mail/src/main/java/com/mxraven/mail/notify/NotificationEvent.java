package com.mxraven.mail.notify;

/**
 * A decoded client notification.
 *
 * <p>{@link #kind()} selects the payload shape. Both kinds arrive on one
 * listener's durable consumer. Branch with pattern matching:
 *
 * <pre>{@code
 * if (event instanceof AcceptedNotification notification) {
 *     // a full inbound message, same payload as a DELIVER_WEBHOOK delivery
 * } else if (event instanceof AttemptNotification attempt) {
 *     // one delivery attempt on the smtp, s3, or webhook channel
 * }
 * }</pre>
 */
public sealed interface NotificationEvent permits AcceptedNotification, AttemptNotification {

    /**
     * Identifies which kind of notification the event carries.
     *
     * @return the event kind
     */
    NotificationKind kind();
}
