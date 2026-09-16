package com.mxraven.admin.model;

/**
 * Wire representation of a submission or MTA listener. Prefer the
 * {@link Listener} entity.
 *
 * @param id unique listener identifier
 * @param tenantId identifier of the owning tenant
 * @param displayName human-readable listener name
 * @param listenerType whether this is a submission or MTA listener
 * @param streamType message stream handled by the listener
 * @param defaultTerminalActionType terminal action applied when no rule matches
 * @param defaultTerminalActionPayload action-specific payload for the default action
 * @param rspamdScanningEnabled whether inbound Rspamd scanning is enabled
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
