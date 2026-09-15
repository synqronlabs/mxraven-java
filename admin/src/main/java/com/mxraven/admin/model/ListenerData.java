package com.mxraven.admin.model;

/**
 * Wire representation of a submission or MTA listener. Prefer the
 * {@link Listener} entity.
 */
public record ListenerData(
        String id,
        String tenantId,
        String displayName,
        ListenerType listenerType,
        StreamType streamType,
        TerminalActionType defaultTerminalActionType,
        TerminalActionPayload defaultTerminalActionPayload,
        boolean rspamdScanningEnabled) {
}
