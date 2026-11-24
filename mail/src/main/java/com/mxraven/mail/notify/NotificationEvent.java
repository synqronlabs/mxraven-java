package com.mxraven.mail.notify;

/**
 * A decoded client notification.
 *
 * <p>{@link #kind()} selects the payload shape. Both kinds arrive on one
 * listener's durable consumer. Branch with pattern matching:
 *
 * <pre>{@code
 * if (event instanceof AcceptedNotification notification) {
 *     // a message, its routing-script decision, and raw-message access
 * } else if (event instanceof AttemptNotification attempt) {
 *     // one delivery attempt on the smtp or s3 channel
 * } else if (event instanceof FaultNotification fault) {
 *     // an MTA script faulted and deferred the transaction
 * }
 * }</pre>
 */
public sealed interface NotificationEvent permits AcceptedNotification, AttemptNotification, FaultNotification {

    /**
     * Identifies which kind of notification the event carries.
     *
     * @return the event kind
     */
    NotificationKind kind();
}
