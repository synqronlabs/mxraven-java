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
 * @param rspamdScanningEnabled whether inbound Rspamd scanning is enabled
 * @param activeScriptRevisionId the published routing-script revision, when one exists
 * @param scriptBodyAccess whether the routing script may read the text/plain body (MTA only)
 */
public record ListenerData(
        String id,
        String tenantId,
        String displayName,
        ListenerType listenerType,
        StreamType streamType,
        boolean rspamdScanningEnabled,
        String activeScriptRevisionId,
        boolean scriptBodyAccess) {
}
