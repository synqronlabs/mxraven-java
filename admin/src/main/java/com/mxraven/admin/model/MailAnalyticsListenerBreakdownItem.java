package com.mxraven.admin.model;

/**
 * One ranked listener in an overview listener breakdown.
 *
 * @param listenerId listener identifier
 * @param displayName human-readable listener name
 * @param listenerState current or deleted/unknown listener state
 * @param egressTasks egress tasks attributed to the listener
 */
public record MailAnalyticsListenerBreakdownItem(
        String listenerId,
        String displayName,
        MailAnalyticsListenerState listenerState,
        long egressTasks) {
}
