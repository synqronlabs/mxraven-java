package com.mxraven.admin.model;

/**
 * Client notification setting of a listener.
 *
 * <p>When enabled, the listener publishes client notification events on
 * {@code subject}; clients bind the durable pull {@code consumer} to read them.
 *
 * @param enabled whether the listener publishes client notification events
 * @param subject subject prefix client notifications are published on for this listener
 * @param consumer durable pull consumer a client binds to in order to read those events
 */
public record ListenerNotificationSetting(
        boolean enabled,
        String subject,
        String consumer) {
}
