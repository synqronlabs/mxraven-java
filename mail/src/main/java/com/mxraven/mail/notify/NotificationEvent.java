package com.mxraven.mail.notify;

/**
 * A decoded client notification.
 *
 * <p>{@link #kind()} selects the payload shape. All kinds arrive on one
 * listener's durable consumer:
 *
 * <pre>
 * if (event instanceof AcceptedNotification) {
 *     // a message, its routing-script decision, and raw-message access
 * } else if (event instanceof AttemptNotification) {
 *     // one delivery attempt on the smtp or s3 channel
 * } else if (event instanceof FaultNotification) {
 *     // an MTA script faulted and deferred the transaction
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
