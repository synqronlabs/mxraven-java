package com.mxraven.mail.notify;

/**
 * A message the listener accepted and whose routing-script decision was
 * resolved.
 */
public final class AcceptedNotification implements NotificationEvent {
    private final AcceptedMessage message;

    /**
     * Creates an accepted notification.
     *
     * @param message the accepted-message payload
     */
    public AcceptedNotification(AcceptedMessage message) {
        this.message = message;
    }

    /**
     * Returns the accepted-message payload.
     *
     * @return the accepted message
     */
    public AcceptedMessage message() {
        return message;
    }

    @Override
    public NotificationKind kind() {
        return NotificationKind.ACCEPTED;
    }
}
