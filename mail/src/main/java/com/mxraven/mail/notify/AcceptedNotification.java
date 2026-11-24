package com.mxraven.mail.notify;

/**
 * A message the listener accepted and whose routing-script decision was
 * resolved.
 *
 * @param message the accepted-message payload; use {@link AcceptedMessage#parse()}
 *                to download the raw message when the listener grants access
 */
public record AcceptedNotification(AcceptedMessage message) implements NotificationEvent {

    @Override
    public NotificationKind kind() {
        return NotificationKind.ACCEPTED;
    }
}
