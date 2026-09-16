package com.mxraven.admin.model;

/**
 * Result of claiming workspace access for an IdP-provisioned user.
 *
 * @param granted whether workspace access was granted
 */
public record IdentityAccessClaim(
        boolean granted) {
}
