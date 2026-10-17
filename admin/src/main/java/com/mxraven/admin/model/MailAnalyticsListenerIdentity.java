package com.mxraven.admin.model;

/**
 * Identity of the listener a mail-analytics overview describes.
 *
 * @param listenerId unique listener identifier
 * @param displayName human-readable listener name
 * @param listenerState whether the listener still exists
 */
public record MailAnalyticsListenerIdentity(
        String listenerId,
        String displayName,
        MailAnalyticsListenerState listenerState) {
}
