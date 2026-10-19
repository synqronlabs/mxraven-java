package com.mxraven.mail.notify;

/**
 * A decoded client notification.
 *
 * <p>{@link #kind()} selects the payload shape. Both kinds arrive on one
 * listener's durable consumer:
 *
 * <pre>
 * if (event instanceof AcceptedNotification) {
 *     // a full inbound message, same payload as a DELIVER_WEBHOOK delivery
 * } else if (event instanceof AttemptNotification) {
 *     // one delivery attempt on the smtp, s3, or webhook channel
 * }
 * </pre>
 */
public interface NotificationEvent {

    /**
     * Identifies which kind of notification the event carries.
     *
     * @return the event kind
     */
    NotificationKind kind();
}
