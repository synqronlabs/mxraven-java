package com.mxraven.admin.model;

/**
 * The principal that approved a connected-application installation.
 *
 * @param issuer identity-provider issuer
 * @param subject identity-provider subject
 * @param email email address recorded at approval time
 */
public record ConnectedAppGrantor(
        String issuer,
        String subject,
        String email) {
}
