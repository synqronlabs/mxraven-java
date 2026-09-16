package com.mxraven.admin.model;

/**
 * An API key together with its one-time plaintext secret.
 *
 * <p>{@code secret} is returned exactly once by the create operation and cannot
 * be retrieved again. Do not blindly retry an uncertain create response.
 *
 * @param id unique API key identifier
 * @param tenantId identifier of the owning tenant
 * @param listenerId identifier of the listener the key authenticates to
 * @param username SMTP username associated with the key
 * @param secret one-time plaintext secret, returned only by the create operation
 */
public record IssuedAPIKey(
        String id,
        String tenantId,
        String listenerId,
        String username,
        String secret) {
}
