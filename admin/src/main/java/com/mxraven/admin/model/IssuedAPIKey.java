package com.mxraven.admin.model;

/**
 * An API key together with its one-time plaintext secret.
 *
 * <p>{@code secret} is returned exactly once by the create operation and cannot
 * be retrieved again. Do not blindly retry an uncertain create response.
 */
public record IssuedAPIKey(
        String id,
        String tenantId,
        String listenerId,
        String username,
        String secret) {
}
