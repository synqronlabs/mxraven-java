package com.mxraven.admin.model;

/**
 * One ranked listener in an overview listener breakdown.
 */
public record MailAnalyticsListenerBreakdownItem(
        String listenerId,
        String displayName,
        MailAnalyticsListenerState listenerState,
        long egressTasks) {
}
