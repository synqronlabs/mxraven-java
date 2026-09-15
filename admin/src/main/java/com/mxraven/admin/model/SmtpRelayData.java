package com.mxraven.admin.model;

/** Wire representation backing the {@link SmtpRelay} entity. */
public record SmtpRelayData(
        String id,
        String tenantId,
        String relayRef,
        String displayName,
        boolean isActive,
        boolean credentialsPresent) {
}
