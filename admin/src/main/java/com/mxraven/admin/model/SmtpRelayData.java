package com.mxraven.admin.model;

/**
 * Wire representation backing the {@link SmtpRelay} entity.
 *
 * @param id unique relay identifier
 * @param tenantId identifier of the owning tenant
 * @param relayRef stable reference for the relay
 * @param displayName human-readable relay name
 * @param isActive whether the relay is active
 * @param credentialsPresent whether relay credentials are stored
 */
public record SmtpRelayData(
        String id,
        String tenantId,
        String relayRef,
        String displayName,
        boolean isActive,
        boolean credentialsPresent) {
}
